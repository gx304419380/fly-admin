package com.fly.admin.system.service;

import com.fly.admin.common.exception.BaseException;
import com.fly.admin.common.util.CacheUtils;
import com.fly.admin.common.util.KeyUtils;
import com.fly.admin.system.dto.LoginDto;
import com.fly.admin.system.dto.RegisterDto;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.system.entity.Account;
import com.fly.admin.system.entity.User;
import com.fly.admin.system.mapper.AccountMapper;
import com.fly.admin.system.wrapper.AccountQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import java.util.Objects;
import java.util.UUID;

import static com.fly.admin.common.constant.CommonConstant.DEV;
import static com.fly.admin.common.constant.CommonConstant.NOT_DELETED;
import static com.fly.admin.common.constant.SystemErrorMessage.*;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@Service
@Slf4j
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private UserService userService;

    @Resource
    private Environment environment;

    /**
     * 校验用户名密码
     *
     * @param dto       dto
     * @return          是否正确
     */
    public UserInfo login(LoginDto dto) {
        String username = dto.getUsername();
        String password = getPassword(dto);

        AccountQuery query = accountMapper.query().where()
                .username().eq(username)
                .deleted().eq(NOT_DELETED)
                .end();

        Account account = accountMapper.findOne(query);
        String salt = account.getSalt();
        String encryptPassword = account.getPassword();

        String passwordAndSalt = password + salt;
        String md5 = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());

        Assert.isTrue(md5.equals(encryptPassword), LOGIN_ERROR);

        //生成token，保存用户信息
        String token = UUID.randomUUID().toString();
        User user = userService.getById(account.getUserId());
        UserInfo userInfo = new UserInfo(user, token, username);

        CacheUtils.put(token, userInfo);
        return userInfo;
    }


    /**
     * 用户注册
     *
     * @param dto  注册信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDto dto) {
        String username = dto.getUsername();
        String password = getPassword(dto);

        String salt = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        String passwordAndSalt = password + salt;
        String md5 = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());

        //保存账户信息
        Account account = new Account();

        account.setUsername(username)
                .setSalt(salt)
                .setPassword(md5)
                .setUserId(userId);
        try {
            accountMapper.save(account);
        } catch (DuplicateKeyException e) {
            throw new BaseException(USERNAME_EXIST_ERROR, e);
        }

        //生成关联用户
        User user = new User()
                .setUserId(userId)
                .setName(username);

        userService.save(user);
    }


    /**
     * 登出
     */
    public void logout(String token) {
        CacheUtils.remove(token);
    }


    /**
     * 获取密码
     *
     * @param dto   dto
     * @return      密码
     */
    private String getPassword(LoginDto dto) {

        String activeProfile = environment.getActiveProfiles()[0];
        boolean isDev = Objects.equals(activeProfile, DEV);
        //如果是开发环境，则直接获取password
        if (isDev) {
            return dto.getPassword();
        }
        //其他环境，需要密钥对

        String key = KeyUtils.getPrivateKey(dto.getPublicKey());
        Assert.hasText(key, KEY_NOT_EXISTS_ERROR);
        return KeyUtils.decrypt(dto.getPassword(), key);
    }


    /**
     * 根据用户名查询
     *
     * @param username  username
     * @return          账户
     */
    public Account getByUsername(String username) {
        AccountQuery query = accountMapper.query()
                .where()
                .username().eq(username)
                .deleted().eq(NOT_DELETED)
                .end();
        return accountMapper.findOne(query);
    }

}

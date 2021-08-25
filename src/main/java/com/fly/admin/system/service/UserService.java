package com.fly.admin.system.service;

import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.common.util.CacheUtils;
import com.fly.admin.system.dto.UserDto;
import com.fly.admin.system.entity.Account;
import com.fly.admin.system.entity.User;
import com.fly.admin.system.event.AccountLoginEvent;
import com.fly.admin.system.event.AccountRegisterEvent;
import com.fly.admin.system.mapper.AccountMapper;
import com.fly.admin.system.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.util.UUID;

import static com.fly.admin.common.constant.CommonConstant.*;
import static com.fly.admin.common.constant.SystemErrorMessage.USER_NULL_ERROR;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountService accountService;

    /**
     * 根据id查询
     *
     * @param userId id
     * @return      用户
     */
    public User getById(String userId) {
        return userMapper.findById(userId);
    }

    /**
     * 保存用户
     *
     * @param dto  user
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDto dto) {

        String userId = dto.getUserId();

        User user = userMapper.findById(userId);
        Assert.notNull(user, USER_NULL_ERROR);

        //更新数据
        User u = dto.convertTo();
        userMapper.updateById(u);

        //获取最新的数据
        user = userMapper.findById(userId);

        //更新缓存的用户数据
        refreshCacheUser(user, null);
    }


    /**
     * 新增用户
     *
     * @param dto   user dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(UserDto dto) {
        String userId = UUID.randomUUID().toString();

        User user = dto.convertTo().setUserId(userId);
        userMapper.save(user);

        Account account = accountService.generateAccount(userId, dto.getUsername(), dto.getPassword());
        accountService.saveAccount(account);

        //更新缓存的用户数据
        refreshCacheUser(user, account);
    }

    /**
     * 更新缓存的用户数据
     *
     * @param user      user
     * @param account   account
     */
    private void refreshCacheUser(User user, Account account) {
        String userId = user.getUserId();

        UserInfo userInfo = CacheUtils.get(USER_ID_CACHE + userId);
        if (isNull(userInfo)) {
            return;
        }

        userInfo.refresh(user, account);
    }

    /**
     * 用户登录后，向缓存中保存token和用户信息
     *
     * @param event event
     */
    @EventListener
    public void onAccountLoginEvent(AccountLoginEvent event) {
        log.info("- receive user login event: {}", event);
        String userId = event.getUserId();
        String token = event.getToken();
        String username = event.getUsername();

        User user = userMapper.findById(userId);
        UserInfo userInfo = new UserInfo(user, token, username);

        //将数据保存在缓存中
        CacheUtils.put(TOKEN_CACHE + token, userInfo);
        CacheUtils.put(USER_ID_CACHE + userId, userInfo);
    }


    /**
     * 用户注册后，生成对应的用户数据，默认名称为username
     *
     * @param event     注册事件
     */
    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onAccountRegisterEvent(AccountRegisterEvent event) {

        String userId = event.getUserId();
        String username = event.getUsername();

        User user = new User(userId, username);

        userMapper.save(user);
    }
}

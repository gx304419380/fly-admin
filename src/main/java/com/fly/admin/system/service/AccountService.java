package com.fly.admin.system.service;

import com.fly.admin.system.entity.Account;
import com.fly.admin.system.mapper.AccountMapper;
import com.fly.admin.system.wrapper.AccountQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import static com.fly.admin.common.constant.CommonConstant.NOT_DELETED;

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

    /**
     * 校验用户名密码
     *
     * @param username  username
     * @param password  password
     * @return          是否正确
     */
    public Boolean validate(String username, String password) {

        AccountQuery query = accountMapper.query().where()
                .username().eq(username)
                .deleted().eq(NOT_DELETED)
                .end();

        Account account = accountMapper.findOne(query);
        String salt = account.getSalt();
        String encryptPassword = account.getPassword();

        String passwordAndSalt = password + salt;
        String md5 = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());
        return md5.equals(encryptPassword);
    }
}

package com.fly.admin.system.controller;

import com.fly.admin.common.dto.Res;
import com.fly.admin.common.util.KeyUtils;
import com.fly.admin.system.dto.LoginDto;
import com.fly.admin.system.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.KeyPairGenerator;

import static com.fly.admin.common.constant.SystemErrorMessage.KEY_NOT_EXISTS_ERROR;
import static com.fly.admin.common.constant.SystemErrorMessage.LOGIN_ERROR;

/**
 * 账号登录登出注册
 *
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@RestController
@RequestMapping("login")
@Slf4j
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("key")
    @ApiOperation("获取公钥")
    public Res<String> key() {
        String publicKey = KeyUtils.generateKeyPair();
        return Res.ok(publicKey);
    }

    @PostMapping
    @ApiOperation("登录校验并返回token")
    public Res<String> login(@Validated @RequestBody LoginDto dto) {
        log.info("- user login: {}", dto);
        String key = KeyUtils.getPrivateKey(dto.getPublicKey());

        Assert.hasText(key, KEY_NOT_EXISTS_ERROR);
        String password = KeyUtils.decrypt(dto.getPassword(), key);

        Boolean validate = accountService.validate(dto.getUsername(), password);
        Assert.isTrue(validate, LOGIN_ERROR);

        log.info("- user login finish");
        return Res.ok();
    }

}

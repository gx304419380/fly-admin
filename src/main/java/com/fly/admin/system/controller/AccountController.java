package com.fly.admin.system.controller;

import com.fly.admin.common.dto.Res;
import com.fly.admin.common.util.KeyUtils;
import com.fly.admin.system.dto.LoginDto;
import com.fly.admin.system.dto.RegisterDto;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.system.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.fly.admin.common.constant.CommonConstant.DEV;

/**
 * 账号登录登出注册
 *
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@RestController
@RequestMapping("account")
@Slf4j
@Api(tags = "登陆注册")
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("key")
    @ApiOperation("获取公钥")
    public Res<String> key() {
        String publicKey = KeyUtils.generateKeyPair();
        return Res.ok(publicKey);
    }


    @PostMapping("register")
    @ApiOperation("注册用户名密码")
    public Res<String> register(@Validated @RequestBody RegisterDto dto) {
        log.info("- register user: {}", dto);

        accountService.register(dto);

        log.info("register success");
        return Res.ok();
    }


    @PostMapping("login")
    @ApiOperation("登录校验并返回token")
    public Res<String> login(@Validated @RequestBody LoginDto dto) {
        log.info("- user login: {}", dto);

        String token = accountService.login(dto);

        log.info("- user login finish");
        return Res.ok(token);
    }


    @PostMapping("loginForTest")
    @ApiOperation("用于自测获取token")
    public String loginForTest(@Validated LoginDto dto) {
        log.info("- user login: {}", dto);

        String token = accountService.login(dto);

        log.info("- user login finish");
        return token;
    }


    @PostMapping("logout")
    @ApiOperation("登出")
    public Res<UserInfo> logout(@RequestHeader String token) {
        log.info("- user logout: {}", token);

        accountService.logout(token);

        log.info("- user logout finish");
        return Res.ok();
    }

}

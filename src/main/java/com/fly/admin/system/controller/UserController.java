package com.fly.admin.system.controller;

import com.fly.admin.common.dto.Res;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.common.util.UserUtils;
import com.fly.admin.system.dto.UserCondition;
import com.fly.admin.system.dto.UserDto;
import com.fly.admin.system.entity.User;
import com.fly.admin.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.fly.admin.common.constant.SystemErrorMessage.USER_ID_NULL_ERROR;

/**
 * 获取用户信息
 *
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/12
 */
@RestController
@RequestMapping("user")
@Slf4j
@Api(tags = "用户")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("page")
    @ApiOperation("分页查询用户信息")
    public Res<Object> page(@RequestBody UserCondition userCondition) {
        return Res.ok();
    }

    @GetMapping
    @ApiOperation("获取当前登录用户信息")
    public Res<UserInfo> getUserInfo() {
        UserInfo userInfo = UserUtils.getUserInfo();
        return Res.ok(userInfo);
    }

    @PutMapping
    @ApiOperation("修改用户")
    public Res<Object> update(@RequestBody UserDto userDto) {
        log.info("- save user: {}", userDto);

        Assert.hasText(userDto.getUserId(), USER_ID_NULL_ERROR);
        userService.update(userDto);

        log.info("- save user finish");
        return Res.ok();
    }

    @PostMapping
    @ApiOperation("新增用户")
    public Res<Object> saveUser(@RequestBody UserDto userDto) {
        log.info("- save user: {}", userDto);

        userService.save(userDto);

        log.info("- save user finish");
        return Res.ok();
    }


    @GetMapping("search")
    public Res<User> getByName(String name) {
        return Res.ok();
    }
}

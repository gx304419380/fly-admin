package com.fly.admin.system.controller;

import cn.org.atool.fluent.mybatis.If;
import com.fly.admin.common.dto.Res;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.common.util.UserUtils;
import com.fly.admin.system.dto.UserDto;
import com.fly.admin.system.entity.User;
import com.fly.admin.system.mapper.UserMapper;
import com.fly.admin.system.mapper.UserXmlMapper;
import com.fly.admin.system.wrapper.UserQuery;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserXmlMapper userXmlMapper;



    @GetMapping
    @ApiOperation("获取用户信息")
    public Res<UserInfo> getUserInfo() {
        UserInfo userInfo = UserUtils.getUserInfo();
        return Res.ok(userInfo);
    }

    @PostMapping
    @ApiOperation("新增、修改用户")
    public Res<Object> saveUser(@RequestBody UserDto userDto) {

        return Res.ok();
    }


    @GetMapping("search")
    public Res<User> getByName(String name) {
        User search = userXmlMapper.search(name);
        return Res.ok(search);
    }
}

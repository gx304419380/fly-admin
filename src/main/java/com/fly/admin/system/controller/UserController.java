package com.fly.admin.system.controller;

import cn.org.atool.fluent.mybatis.If;
import com.fly.admin.common.dto.Res;
import com.fly.admin.system.entity.User;
import com.fly.admin.system.mapper.UserMapper;
import com.fly.admin.system.mapper.UserXmlMapper;
import com.fly.admin.system.wrapper.UserQuery;
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

    @GetMapping("{id}")
    public Res<User> getById(@PathVariable String id, @RequestParam String name) {

        userMapper.deleteById();

        UserQuery query = userMapper.query()
                .select.userId().end()
                .where()
                .name().like(name, StringUtils::hasText)
                .userId().eq(id, If::notBlank)
                .end();
        User one = userMapper.findOne(query);

        return Res.ok(one);
    }


    @GetMapping("search")
    public Res<User> getByName(String name) {
        User search = userXmlMapper.search(name);
        return Res.ok(search);
    }
}

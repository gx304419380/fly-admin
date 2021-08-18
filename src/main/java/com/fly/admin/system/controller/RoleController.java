package com.fly.admin.system.controller;

import com.fly.admin.common.dto.Res;
import com.fly.admin.system.entity.Role;
import com.fly.admin.system.service.RoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色接口
 *
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@RestController
@RequestMapping("role")
@Slf4j
@Api(tags = "角色")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping
    public Res<List<Role>> listAll() {
        List<Role> list = roleService.list();
        return Res.ok(list);
    }

}

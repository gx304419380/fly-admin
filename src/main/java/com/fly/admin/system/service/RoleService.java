package com.fly.admin.system.service;

import com.fly.admin.system.entity.Role;
import com.fly.admin.system.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/17
 */
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    public List<Role> list() {
        return roleMapper.listEntity(roleMapper.query());
    }

}

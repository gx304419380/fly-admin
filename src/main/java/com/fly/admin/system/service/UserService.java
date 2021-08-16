package com.fly.admin.system.service;

import com.fly.admin.system.entity.User;
import com.fly.admin.system.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

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
     * @param user  user
     */
    public void save(User user) {
        userMapper.save(user);
    }
}

package com.fly.admin.system.service;

import com.fly.admin.system.entity.Menu;
import com.fly.admin.system.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Service
public class MenuService {

    @Resource
    private MenuMapper menuMapper;

    public Menu saveMenu(Menu menu) {
        menuMapper.save(menu);
        return menu;
    }


    public void tree() {


    }
}

package com.fly.admin.system.controller;

import com.fly.admin.common.dto.Res;
import com.fly.admin.system.dto.AppDto;
import com.fly.admin.system.service.MenuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@RestController
@RequestMapping("menu")
@Slf4j
@Api(tags = "菜单权限")
public class MenuController {

    @Resource
    private MenuService menuService;


}

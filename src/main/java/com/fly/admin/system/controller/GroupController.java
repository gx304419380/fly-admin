package com.fly.admin.system.controller;

import com.fly.admin.system.dto.GroupDto;
import com.fly.admin.common.dto.Res;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.common.util.UserUtils;
import com.fly.admin.system.entity.Group;
import com.fly.admin.system.service.GroupService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.fly.admin.common.constant.SystemErrorMessage.USER_NULL_ERROR;

/**
 * 用户数据权限分组接口，例如部门、组织等
 *
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@RestController
@RequestMapping("group")
@Slf4j
public class GroupController {
    
    @Resource
    private GroupService groupService;



    @ApiOperation("根据id查询信息")
    @GetMapping("{id}")
    public Res<Group> getGroup(@PathVariable String id) {
        UserInfo user = UserUtils.getUserInfo();
        Assert.notNull(user, USER_NULL_ERROR);

        Group group = groupService.getById(id);

        return Res.ok(group);
    }


    @ApiOperation("查询整棵树")
    @GetMapping("fullTree")
    public Res<List<GroupDto>> getFullTree() {
        UserInfo user = UserUtils.getUserInfo();
        Assert.notNull(user, USER_NULL_ERROR);

        List<GroupDto> tree = groupService.getFullTree(user);

        return Res.ok(tree);
    }


    @ApiOperation("异步树，查询下一级")
    @GetMapping("next")
    public Res<List<GroupDto>> nextLevel(@RequestParam(required = false) String parentId) {
        UserInfo user = UserUtils.getUserInfo();
        Assert.notNull(user, USER_NULL_ERROR);

        List<GroupDto> list = groupService.nextLevel(user, parentId);

        return Res.ok(list);
    }


    @ApiOperation("查询所有group")
    @GetMapping("location")
    public Res<List<Group>> getLocation(@RequestParam(required = false) Long parentId) {
        UserInfo user = UserUtils.getUserInfo();
        Assert.notNull(user, USER_NULL_ERROR);

        List<Group> list = groupService.getLocation(user, parentId);

        return Res.ok(list);
    }



    @ApiOperation("按名称查询")
    @GetMapping("search")
    public Res<List<Group>> searchByName(@RequestParam String name) {
        UserInfo user = UserUtils.getUserInfo();
        Assert.notNull(user, USER_NULL_ERROR);

        List<Group> list = groupService.searchByName(user, name);

        return Res.ok(list);
    }


    @ApiOperation("新增或修改")
    @PostMapping
    public Res<String> saveOrUpdate(@RequestBody @Validated GroupDto dto) {
        UserInfo user = UserUtils.getUserInfo();

        Assert.notNull(user, USER_NULL_ERROR);
        log.info("- saveOrUpdate Group by user: {}", user.getUsername());

        groupService.saveOrUpdate(dto, user);

        log.info("- saveOrUpdate Group finish");
        return Res.ok();
    }


    @ApiOperation("根据id删除节点")
    @DeleteMapping("{id}")
    public Res<String> deleteById(@PathVariable @NotNull Long id) {
        UserInfo user = UserUtils.getUserInfo();

        Assert.notNull(user, USER_NULL_ERROR);
        log.info("- delete Group by user: {}, id = {}", user.getUsername(), id);

        groupService.deleteById(id, user);
        log.info("- delete Group finish");
        return Res.ok();
    }


}

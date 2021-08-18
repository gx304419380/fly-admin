package com.fly.admin.system.service;

import com.fly.admin.system.dto.GroupDto;
import com.fly.admin.common.dto.UserInfo;
import com.fly.admin.system.entity.Group;
import com.fly.admin.system.mapper.GroupMapper;
import com.fly.admin.system.mapper.GroupXmlMapper;
import com.fly.admin.system.wrapper.GroupQuery;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.util.*;

import static com.fly.admin.common.constant.CommonConstant.*;
import static com.fly.admin.common.constant.SystemErrorMessage.GROUP_NULL_ERROR;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/18
 */
@Service
@Slf4j
public class GroupService {

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private GroupXmlMapper groupXmlMapper;

    /**
     * 根据id查询
     *
     * @param id    id
     * @return      group
     */
    public Group getById(String id) {
        GroupQuery query = groupMapper.query()
                .where()
                .id().eq(id)
                .deleted().eq(NOT_DELETED)
                .end();

        return groupMapper.findOne(query);
    }


    /**
     * 查询下一级
     *
     * @param user      user
     * @param parentId  parentId
     * @return          下一级list
     */
    public List<GroupDto> nextLevel(UserInfo user, String parentId) {
        //返回根节点
        if (isNull(parentId)) {
            Group group = groupMapper.findById(ROOT);
            GroupDto root = new GroupDto(group).hasPermission();
            return singletonList(root);
        }

        //查询子节点
        String groupId = user.getGroupId();
        if (ROOT.equals(groupId)) {
            List<Group> children = getByParentId(parentId);
            return children.stream().map(GroupDto::new).map(GroupDto::hasPermission).collect(toList());
        }

        //判断是否有权限
        Group group = getById(groupId);
        String path = group.getPath();
        String[] parentIdArray = path.split(PATH_JOIN);
        String[] idArray = Arrays.copyOf(parentIdArray, parentIdArray.length - 1);
        List<String> parentList = Arrays.asList(idArray);

        return groupXmlMapper.getByPathAndParent(path, parentList, parentId);
    }

    /**
     * 根据父节点查询
     *
     * @param parentId parentId
     * @return          list
     */
    public List<Group> getByParentId(String parentId) {
        GroupQuery query = new GroupQuery()
                .where()
                .deleted().eq(NOT_DELETED)
                .parentId().eq(parentId)
                .end();

        return groupMapper.listEntity(query);
    }


    /**
     * 获取用户有权限的整棵树
     *
     * @param user  user
     * @return      用户有权限的整棵树
     */
    public List<GroupDto> getFullTree(UserInfo user) {

        String groupId = user.getGroupId();

        //如果是根节点开始查的，则直接返回整棵树
        if (ROOT.equals(groupId)) {
            return getFullTree();
        }

        Group group = getById(groupId);
        Assert.notNull(group, GROUP_NULL_ERROR);

        String path = group.getPath();
        String[] parentIdArray = path.split(PATH_JOIN);
        String[] idArray = Arrays.copyOf(parentIdArray, parentIdArray.length - 1);
        List<String> parentList = Arrays.asList(idArray);

        List<GroupDto> list = groupXmlMapper.getByPathAndParent(path, parentList, null);

        return listToTree(list);
    }




    /**
     * 查询整棵树
     *
     * @return  整棵树
     */
    private List<GroupDto> getFullTree() {
        GroupQuery query = groupMapper.query()
                .where()
                .deleted().eq(NOT_DELETED)
                .end()
                .orderBy.updateTime().desc()
                .end();

        List<GroupDto> list = groupMapper.listEntity(query)
                .stream()
                .map(GroupDto::new)
                .map(GroupDto::hasPermission)
                .collect(toList());

        return listToTree(list);
    }

    /**
     * list转为tree
     *
     * @param list              list
     * @return                  树
     */
    private List<GroupDto> listToTree(List<GroupDto> list) {
        List<GroupDto> treeList = new ArrayList<>();

        for (GroupDto group : list) {
            if (ROOT.equals(group.getId())) {
                treeList.add(group);
            }

            List<GroupDto> children = list.stream()
                    .filter(d -> Objects.equals(d.getParentId(), group.getId()))
                    .collect(toList());
            group.setChildren(children);
        }

        return treeList;
    }


}

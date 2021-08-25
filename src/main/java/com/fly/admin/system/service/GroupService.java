package com.fly.admin.system.service;

import cn.org.atool.fluent.mybatis.If;
import com.fly.admin.common.util.Assert;
import com.fly.admin.system.dto.GroupDto;
import com.fly.admin.system.entity.Group;
import com.fly.admin.system.event.GroupEvent;
import com.fly.admin.system.mapper.GroupMapper;
import com.fly.admin.system.mapper.GroupXmlMapper;
import com.fly.admin.system.wrapper.GroupQuery;
import com.fly.admin.system.wrapper.GroupUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.fly.admin.common.constant.CommonConstant.*;
import static com.fly.admin.common.constant.EventType.*;
import static com.fly.admin.common.constant.SystemErrorMessage.*;
import static com.fly.admin.common.util.Check.*;
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

    @Resource
    private ApplicationEventPublisher publisher;

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
     * 根据id list查询
     *
     * @param idList    id
     * @return      group list
     */
    public List<Group> getByIdList(Collection<String> idList) {
        GroupQuery query = groupMapper.query()
                .where()
                .id().in(idList)
                .deleted().eq(NOT_DELETED)
                .end()
                .orderBy.updateTime().desc()
                .end();

        return groupMapper.listEntity(query);
    }


    /**
     * 新增或者修改，重名校验通过数据库约束
     * 加锁保障数据一致
     *
     * @param dto   dto
     * @param userGroupId 用户有权限的分组
     * @param userId    用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(GroupDto dto, String userGroupId, String userId) {
        //校验权限
        String parentId = dto.getParentId();

        //校验名称
        Boolean existName = existName(dto.getName(), dto.getId());
        Assert.isFalse(existName, GROUP_NAME_EXIST_ERROR);

        Group parent = hasPermission(userGroupId, parentId);
        Assert.notNull(parent, HAS_NO_PERMISSION_ERROR);

        Group group = dto.convertTo().setUpdateUser(userId);

        //id not null -> update group
        if (notEmpty(group.getId())) {
            updateGroup(group, parent);
            return;
        }

        //新增分组
        String id = UUID.randomUUID().toString();
        String name = group.getName();
        group.setId(id)
                .setPath(generatePath(id, parent.getPath()))
                .setNamePath(generatePath(name, parent.getNamePath()))
                .setLevel(parent.getLevel() + 1)
                .setHasChild(NO_CHILD)
                .setCreateUser(userId);

        groupMapper.insert(group);
        publisher.publishEvent(new GroupEvent(ADD, group));
    }


    /**
     * 更新分组
     *
     * @param group     group
     * @param parent    父节点
     */
    private void updateGroup(Group group, Group parent) {
        String id = group.getId();
        String name = group.getName();

        Group old = getById(id);
        String oldPath = old.getPath();
        String oldNamePath = old.getNamePath();
        String oldParentId = old.getParentId();

        String path = generatePath(id, parent.getPath());
        String namePath = generatePath(name, parent.getPath());

        group.setCreateUser(old.getCreateUser())
                .setCreateTime(old.getCreateTime())
                .setPath(path)
                .setNamePath(namePath)
                .setLevel(parent.getLevel() + 1);

        //修改父节点has child
        if (NO_CHILD.equals(parent.getHasChild())) {
            parent.setHasChild(HAS_CHILD);
            groupMapper.updateById(parent);
            // TODO: 2021/8/20 发射事件
        }


        //如果变更父节点，则需要处理节点转移逻辑，原父节点hasChild
        if (!oldParentId.equals(parent.getId())) {
            groupXmlMapper.updateGroupChildStatus(oldParentId);
            // TODO: 2021/8/20 发射事件
        }

        //更新节点数据
        groupMapper.updateById(group);

        //如果没有移动节点，并且没有修改节点名称，则直接返回，否则需要修改子节点list
        if (oldParentId.equals(parent.getId()) && oldNamePath.equals(namePath)) {
            publisher.publishEvent(new GroupEvent(UPDATE, group, old));
            return;
        }

        //所有子节点修改name Path level， 然后再循环中更新数据，这里不采用批量更新，因为很少会有转移节点的操作
        List<Group> children = getAllChildren(oldPath);
        if (children.isEmpty()) {
            return;
        }

        List<Group> newChildren = new ArrayList<>(children.size());

        for (Group child : children) {
            String childPath = child.getPath().replace(oldPath, path);
            String childNamePath = child.getNamePath().replace(oldNamePath, namePath);
            Integer childLevel = childPath.split(PATH_JOIN).length - 1;

            Group newChild = new Group();
            BeanUtils.copyProperties(child, newChild);
            newChild.setPath(childPath).setNamePath(childNamePath).setLevel(childLevel);
            groupMapper.updateById(newChild);
            newChildren.add(newChild);
        }

        //发射事件
        newChildren.add(group);
        children.add(old);
        publisher.publishEvent(new GroupEvent(UPDATE, newChildren, children));
    }


    /**
     * 校验名称是否存在
     * @param name  name
     * @param id    id
     * @return      count
     */
    public Boolean existName(String name, String id) {

        GroupQuery query = groupMapper.query()
                .where
                .name().eq(name)
                .id().ne(id, If::notBlank)
                .deleted().eq(NOT_DELETED)
                .end()
                .limit(1);

        return !groupMapper.listEntity(query).isEmpty();
    }

    /**
     * 获取所有子节点 不包括自己
     *
     * @param path 路径
     * @return  children
     */
    public List<Group> getAllChildren(String path) {
        GroupQuery query = groupMapper.query()
                .where()
                .path().likeRight(path)
                .path().ne(path)
                .deleted().eq(NOT_DELETED)
                .end();

        return groupMapper.listEntity(query);
    }

    /**
     * 生成路径
     *
     * @param id    id
     * @param parentPath  路径
     * @return      path
     */
    private String generatePath(String id, String parentPath) {
        return parentPath + id + PATH_JOIN;
    }


    /**
     * 校验用户权限
     *
     * @param userGroupId   用户所属分组
     * @param groupId       groupId
     * @return              如果有权限则返回该分组
     */
    public Group hasPermission(String userGroupId, String groupId) {
        if (isEmpty(userGroupId)) {
            return null;
        }

        if (isEqual(userGroupId, groupId)) {
            return getById(groupId);
        }

        //查询数据库
        Group group = getById(groupId);
        String[] parents = group.getPath().split(PATH_JOIN);

        boolean hasPermission = Arrays.asList(parents).contains(userGroupId);
        return hasPermission ? group : null;
    }


    /**
     * 批量校验用户权限
     *
     * @param userGroupId   用户所属分组
     * @param idList        id list
     * @return              返回用户有权限的分组
     */
    public List<Group> hasPermission(String userGroupId, Collection<String> idList) {
        Group group = getById(userGroupId);
        String path = group.getPath();

        GroupQuery query = groupMapper.query()
                .where()
                .id().in(idList)
                .path().likeRight(path)
                .deleted().eq(NOT_DELETED)
                .end()
                .orderBy.updateTime().desc()
                .end();

        return groupMapper.listEntity(query);
    }


    /**
     * 查询下一级
     *
     * @param groupId   用户所属分组
     * @param parentId  parentId
     * @return          下一级list
     */
    public List<GroupDto> nextLevel(String groupId, String parentId) {
        //返回根节点
        if (isNull(parentId)) {
            Group group = groupMapper.findById(ROOT);
            GroupDto root = new GroupDto(group).hasPermission();
            return singletonList(root);
        }

        //查询子节点
        if (ROOT.equals(groupId)) {
            List<Group> children = getByParentId(parentId);
            return children.stream().map(GroupDto::new).map(GroupDto::hasPermission).collect(toList());
        }

        //判断是否有权限
        Group group = getById(groupId);
        String path = group.getPath();
        List<String> parentList = getParentList(path);

        return groupXmlMapper.getByPathAndParent(path, parentList, parentId);
    }

    /**
     * 获取上级所有节点id
     *
     * @param path  id路径
     * @return      list
     */
    private List<String> getParentList(String path) {
        String[] parentIdArray = path.split(PATH_JOIN);
        String[] idArray = Arrays.copyOfRange(parentIdArray, 1,parentIdArray.length - 1);
        return Arrays.asList(idArray);
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
     * @param groupId 用户所属分组
     * @return      用户有权限的整棵树
     */
    public List<GroupDto> getFullTree(String groupId) {

        //如果是根节点开始查，则直接返回整棵树
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
                    .filter(d -> isEqual(d.getParentId(), group.getId()))
                    .collect(toList());
            group.setChildren(children);
        }

        return treeList;
    }


    /**
     * 根据名称查询用户有权限的list
     *
     * @param groupId 用户所属分组
     * @param name  name
     * @return      list
     */
    public List<Group> searchByName(String groupId, String name) {
        Assert.hasText(groupId, HAS_NO_GROUP_ERROR);

        Group group = getById(groupId);
        GroupQuery query = groupMapper.query()
                .where()
                .deleted().eq(NOT_DELETED)
                .path().likeRight(group.getPath())
                .name().like(name, If::notBlank)
                .end();

        return groupMapper.listEntity(query);
    }


    /**
     * 根据id删除节点
     *
     * @param id    id
     * @param userGroup  userGroup
     * @param userId     userId
     */
    public void deleteById(String id, String userGroup, String userId) {
        Assert.notEqual(ROOT, id, DELETE_ROOT_ERROR);

        //权限校验
        Group group = hasPermission(userGroup, id);
        Assert.notNull(group, HAS_NO_PERMISSION_ERROR);

        //获取所有子节点
        List<Group> children = getAllChildren(group.getPath());

        List<String> list = children.stream().map(Group::getId).collect(toList());
        list.add(id);

        //批量更新
        GroupUpdate update = groupMapper.updater()
                .set.deleted().is(DELETE).end()
                .where.id().in(list).end();

        groupMapper.updateBy(update);

        //发射删除事件
        children.add(group);
        publisher.publishEvent(new GroupEvent(DELETE, children));
    }
}

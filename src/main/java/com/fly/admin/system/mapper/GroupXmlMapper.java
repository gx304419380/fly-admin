package com.fly.admin.system.mapper;

import com.fly.admin.system.dto.GroupDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/18
 */
@Mapper
public interface GroupXmlMapper {
    /**
     * 查询用户可见的group，包含有权限的节点和path路径上的节点
     *
     * @param path          path
     * @param parentList    parentList
     * @param parentId      parentId
     * @return              list
     */
    List<GroupDto> getByPathAndParent(@Param("path") String path,
                                      @Param("parentList") List<String> parentList,
                                      @Param("parentId") String parentId);

    /**
     * 更新节点是否会有子节点
     *
     * @param id    id
     */
    void updateGroupChildStatus(@Param("id") String id);
}

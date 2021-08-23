package com.fly.admin.system.dto;

import com.fly.admin.system.entity.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.fly.admin.common.constant.CommonConstant.HAS_PERMISSION;
import static com.fly.admin.common.constant.CommonConstant.NO_PERMISSION;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/18
 */
@Data
@ApiModel(description = "分组dto")
@NoArgsConstructor
@Accessors(chain = true)
public class GroupDto {

    private String id;

    @ApiModelProperty(value = "父节点id")
    private String parentId;

    @ApiModelProperty(value = "id路径/分割")
    private String path;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    private String namePath;

    /**
     * 类型，例如 org
     */
    private String type;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "是否有子节点，0无，1有")
    private Integer hasChild;

    @ApiModelProperty("权限类型，0无权限，1有权限")
    private Integer permissionType;

    private List<GroupDto> children;

    private Object extension;

    public GroupDto(Group group) {
        BeanUtils.copyProperties(group, this);
    }

    public GroupDto hasPermission() {
        return this.setPermissionType(HAS_PERMISSION);
    }

    public GroupDto noPermission() {
        return this.setPermissionType(NO_PERMISSION);
    }

    public Group convertTo() {
        Group group = new Group();
        BeanUtils.copyProperties(this, group);

        // TODO: 2021/8/20 extension json处理


        return group;
    }
}

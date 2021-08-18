package com.fly.admin.system.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Data
@Accessors(chain = true)
@FluentMybatis(table = "tb_menu")
public class Menu implements IEntity {

    @TableId(auto = false)
    private String id;

    private String parentId;

    private String path;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限码
     */
    private String permissionCode;

    /**
     * 权限类型，例如 menu, button, resource
     */
    private String type;

    /**
     * 树型结构排序
     */
    private Integer sort;

    /**
     * 扩展json
     */
    private String extension;

    @TableField(insert = "now()")
    private LocalDateTime createTime;

    @TableField(insert = "now()", update = "now()")
    private LocalDateTime updateTime;

    private Integer deleted;

    @Override
    public Serializable findPk() {
        return id;
    }
}

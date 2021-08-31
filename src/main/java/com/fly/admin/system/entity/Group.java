package com.fly.admin.system.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/18
 */
@Data
@Accessors(chain = true)
@FluentMybatis(table = "tb_group")
public class Group implements IEntity {

    @TableId(auto = false)
    private String id;

    private String parentId;

    private String path;

    /**
     * 分组名称
     */
    private String name;

    private String namePath;

    private String description;

    /**
     * 类型，例如 org
     */
    private String type;

    private Integer level;

    private Integer hasChild;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createUser;

    private String updateUser;

    private Integer deleted;

    private String extension;

    @Override
    public Serializable findPk() {
        return id;
    }
}

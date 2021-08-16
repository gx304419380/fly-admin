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
 * @since 2021/8/12
 */
@Data
@Accessors(chain = true)
@FluentMybatis(table = "tb_user")
public class User implements IEntity {

    @TableId(auto = false)
    private String userId;

    private String name;

    private String nickName;

    private String phone;

    private String email;

    private String description;

    @TableField(insert = "now()")
    private LocalDateTime createTime;

    @TableField(insert = "now()", update = "now()")
    private LocalDateTime updateTime;

    private Integer deleted;

    @Override
    public Serializable findPk() {
        return userId;
    }
}

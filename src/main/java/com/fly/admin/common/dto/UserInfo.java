package com.fly.admin.common.dto;

import com.fly.admin.system.entity.Account;
import com.fly.admin.system.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserInfo {

    private String userId;

    private String name;

    private String nickName;

    private String phone;

    private String email;

    private String description;

    /**
     * 所属组织id
     */
    private String groupId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String token;

    private String username;

    public UserInfo(User user, String token, String username) {
        BeanUtils.copyProperties(user, this);
        this.token = token;
        this.username = username;
    }

    public void refresh(User user, Account account) {
        BeanUtils.copyProperties(user, this);
        this.username = account.getUsername();
    }
}


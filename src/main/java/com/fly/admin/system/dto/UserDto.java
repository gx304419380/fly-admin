package com.fly.admin.system.dto;

import com.fly.admin.system.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.List;

import static com.fly.admin.common.constant.SystemErrorMessage.NAME_SIZE_ERROR;
import static com.fly.admin.common.constant.SystemErrorMessage.NICKNAME_SIZE_ERROR;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@Data
@Accessors(chain = true)
public class UserDto {

    private String userId;

    @NotBlank
    @Length(min = 1, max = 20, message = NAME_SIZE_ERROR)
    private String name;

    @Length(min = 1, max = 20, message = NICKNAME_SIZE_ERROR)
    private String nickName;

    private String phone;

    @Email
    private String email;

    private String description;

    private List<String> roleList;

    private String groupId;

    private String username;

    private String password;

    public User convertTo() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
}

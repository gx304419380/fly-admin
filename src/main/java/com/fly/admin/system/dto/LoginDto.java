package com.fly.admin.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static com.fly.admin.common.constant.SystemErrorMessage.PASSWORD_BLANK_ERROR;
import static com.fly.admin.common.constant.SystemErrorMessage.USERNAME_BLANK_ERROR;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@Data
@Accessors(chain = true)
public class LoginDto {

    @NotBlank(message = USERNAME_BLANK_ERROR)
    private String username;

    @NotBlank(message = PASSWORD_BLANK_ERROR)
    private String password;

    @NotBlank
    private String publicKey;

    public String getUsername() {
        return username == null ? null : username.trim();
    }

    public String getPassword() {
        return password == null ? null : password.trim();
    }
}

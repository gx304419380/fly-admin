package com.fly.admin.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.fly.admin.common.constant.SystemErrorMessage.*;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@Data
@Accessors(chain = true)
public class LoginDto {

    @NotBlank(message = USERNAME_BLANK_ERROR)
    @Length(min = 4, max = 20, message = USERNAME_SIZE_ERROR)
    private String username;

    @NotBlank(message = PASSWORD_BLANK_ERROR)
    @Length(min = 6, max = 512, message = PASSWORD_SIZE_ERROR)
    private String password;

    private String publicKey;

    public String getUsername() {
        return username == null ? null : username.trim();
    }

    public String getPassword() {
        return password == null ? null : password.trim();
    }
}

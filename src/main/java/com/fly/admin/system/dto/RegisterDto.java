package com.fly.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.fly.admin.common.constant.SystemErrorMessage.*;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RegisterDto extends LoginDto {

}

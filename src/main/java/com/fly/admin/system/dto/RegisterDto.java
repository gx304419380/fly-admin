package com.fly.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

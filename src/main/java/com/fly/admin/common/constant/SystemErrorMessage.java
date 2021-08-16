package com.fly.admin.common.constant;

import lombok.experimental.UtilityClass;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@UtilityClass
public class SystemErrorMessage {

    public static final String KEY_NOT_EXISTS_ERROR = "用户密钥不存在";
    public static final String ENCODE_ERROR = "加密失败";
    public static final String DECODE_ERROR = "解密失败";
    public static final String LOGIN_ERROR = "用户登录失败，请检查用户名和密码";
    public static final String USERNAME_BLANK_ERROR = "用户名不能为空";
    public static final String USERNAME_EXIST_ERROR = "用户名已存在";
    public static final String USERNAME_SIZE_ERROR = "用户名为4-20位";
    public static final String PASSWORD_BLANK_ERROR = "密码不能为空";
    public static final String PASSWORD_SIZE_ERROR = "密码为6到20位";

}

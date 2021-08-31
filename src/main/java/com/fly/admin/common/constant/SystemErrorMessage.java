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
    public static final String USER_NOT_LOGIN = "用户未登录";
    public static final String USER_ID_NULL_ERROR = "用户ID能为空";
    public static final String USER_NULL_ERROR = "用户不存在";
    public static final String USERNAME_EXIST_ERROR = "用户名已存在";
    public static final String USERNAME_SIZE_ERROR = "用户名为4-20位";
    public static final String NAME_SIZE_ERROR = "用户名称为2-20位";
    public static final String NICKNAME_SIZE_ERROR = "用户昵称为2-20位";
    public static final String PASSWORD_BLANK_ERROR = "密码不能为空";
    public static final String PASSWORD_SIZE_ERROR = "密码为6到20位";

    public static final String GROUP_NULL_ERROR = "组织节点不存在";
    public static final String GROUP_NAME_EXIST_ERROR = "组织名称已存在";
    public static final String HAS_NO_PERMISSION_ERROR = "用户无权限";
    public static final String HAS_NO_GROUP_ERROR = "用户未关联组织";
    public static final String DELETE_ROOT_ERROR = "无法删除根节点";
}

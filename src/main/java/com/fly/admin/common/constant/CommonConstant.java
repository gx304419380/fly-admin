package com.fly.admin.common.constant;

import lombok.experimental.UtilityClass;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@UtilityClass
public class CommonConstant {

    public static final Integer DELETED = 1;
    public static final Integer NOT_DELETED = 0;

    public static final String DEV = "dev";
    public static final String TOKEN = "token";
    public static final String TOKEN_CACHE = "token:";
    public static final String USER_ID_CACHE = "userId:";


    public static final String PATH_JOIN = "/";
    public static final String ROOT = "ORG_ROOT";
    public static final String ROOT_PARENT = "ROOT";

    public static final Integer HAS_PERMISSION = 1;
    public static final Integer NO_PERMISSION = 0;
    public static final Integer NO_CHILD = 0;
    public static final Integer HAS_CHILD = 1;
    public static final String ORG = "org";

}

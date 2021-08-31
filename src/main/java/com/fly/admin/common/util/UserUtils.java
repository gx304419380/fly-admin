package com.fly.admin.common.util;

import com.fly.admin.common.dto.UserInfo;
import lombok.experimental.UtilityClass;

import static com.fly.admin.common.util.Check.notNull;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@UtilityClass
public class UserUtils {
    private static final InheritableThreadLocal<UserInfo> USER_HOLDER = new InheritableThreadLocal<>();


    public static void put(UserInfo user) {
        USER_HOLDER.set(user);
    }

    public static void remove() {
        USER_HOLDER.remove();
    }

    public static UserInfo getUserInfo() {
        return USER_HOLDER.get();
    }

    public static String getToken() {
        UserInfo userInfo = USER_HOLDER.get();

        if (notNull(userInfo)) {
            return userInfo.getToken();
        }

        return null;
    }
}

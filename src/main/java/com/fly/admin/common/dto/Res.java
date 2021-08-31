package com.fly.admin.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/12
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Res<T> {

    public static final Integer SUCCESS_CODE = 0;
    public static final Integer FAIL_CODE = 1;
    public static final Integer USER_LOGOUT = 2;

    private Integer code;
    private String message;
    private T data;


    public static <T> Res<T> ok(T data) {
        return new Res<>(SUCCESS_CODE, null, data);
    }

    public static <T> Res<T> ok() {
        return new Res<>(SUCCESS_CODE, null, null);
    }

    public static <T> Res<T> fail(String message) {
        return new Res<>(FAIL_CODE, message, null);
    }

    public static <T> Res<T> fail(Integer code, String message) {
        return new Res<>(code, message, null);
    }
}

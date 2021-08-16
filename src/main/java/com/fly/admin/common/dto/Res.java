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

    public static final Integer SUCCESS = 0;
    public static final Integer FAIL = 1;

    private Integer code;
    private String msg;
    private T data;


    public static <T> Res<T> ok(T data) {
        return new Res<>(SUCCESS, null, data);
    }

    public static <T> Res<T> ok() {
        return new Res<>(SUCCESS, null, null);
    }

    public static <T> Res<T> fail(String msg) {
        return new Res<>(FAIL, msg, null);
    }

}

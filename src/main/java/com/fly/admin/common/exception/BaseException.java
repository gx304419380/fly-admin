package com.fly.admin.common.exception;

import com.fly.admin.common.dto.Res;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/13
 */
@Data
@Accessors(chain = true)
public class BaseException extends RuntimeException {

    private String msg;
    private Integer code;

    public BaseException() {
    }

    public BaseException(String message, Integer code) {
        super(message);
        this.msg = message;
        this.code = code;
    }

    public BaseException(String message) {
        super(message);
        this.msg = message;
        this.code = Res.FAIL;
    }

    public BaseException(Throwable cause) {
        super(cause);
        this.msg = cause.getMessage();
        this.code = Res.FAIL;
    }

    public BaseException(String msg, Exception exception) {
        super(exception);
        this.msg = msg;
        this.code = Res.FAIL;
    }
}

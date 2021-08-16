package com.fly.admin.common.exception;

import com.fly.admin.common.dto.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res<String> handleException(MethodArgumentNotValidException e) {
        log.error("参数验证错误", e);
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return Res.fail(message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Res<String> handleException(HttpRequestMethodNotSupportedException e) {
        log.error("请求方法不支持错误", e);
        return Res.fail(e.getMethod() + "方法不支持");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Res<String> handleException(HttpMessageNotReadableException e) {
        log.error("没有对参数进行json序列化", e);
        return Res.fail("没有对参数进行json序列化");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Res<String> handleException(IllegalArgumentException e) {
        log.error("不合法的参数异常", e);
        return Res.fail(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Res<String> handleException(Exception e) {
        log.error("- unknown exception", e);
        return Res.fail("unknown exception");
    }

    @ExceptionHandler(BaseException.class)
    public Res<String> handleException(BaseException e) {
        log.error("- base exception", e);
        return Res.fail(e.getMsg());
    }


}

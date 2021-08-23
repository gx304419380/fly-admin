package com.fly.admin.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/20
 */
@UtilityClass
public class Assert {


    /**
     * Assert a boolean expression, throwing an {@code IllegalArgumentException}
     * if the expression evaluates to {@code false}.
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a boolean expression
     * if the expression evaluates to {@code true}.
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }



    /**
     * Assert that an object is {@code null}.
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     */
    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }



    /**
     * Assert that an object is not {@code null}.
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     */
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Assert that the given String contains valid text content; that is, it must not
     * be {@code null} and must contain at least one non-whitespace character.
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     */
    public static void hasText(@Nullable String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }



    /**
     * Assert that an array contains elements; that is, it must not be
     * {@code null} and must contain at least one element.
     * @param array the array to check
     * @param message the exception message to use if the assertion fails
     */
    public static void notEmpty(@Nullable Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Assert that a collection contains elements; that is, it must not be
     * {@code null} and must contain at least one element.
     * @param collection the collection to check
     * @param message the exception message to use if the assertion fails
     * contains no elements
     */
    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Assert that a Map contains entries; that is, it must not be {@code null}
     * and must contain at least one entry.
     * @param map the map to check
     * @param message the exception message to use if the assertion fails
     */
    public static void notEmpty(@Nullable Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 如果相等则抛出异常
     *
     * @param a     a
     * @param b     b
     * @param message   message
     */
    public static void notEqual(Object a, Object b, String message) {
        if (Objects.equals(a, b)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 如果不相等则抛出异常
     *
     * @param a     a
     * @param b     b
     * @param message   message
     */
    public static void isEqual(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new IllegalArgumentException(message);
        }
    }
}

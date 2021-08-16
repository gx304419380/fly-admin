package com.fly.admin.common.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author guoxiang
 * @version 1.0.0
 * @since 2021/8/16
 */
@UtilityClass
@Slf4j
public class CacheUtils {

    /**
     * TODO: 2021/8/16 换成redis
     */
    private static final Cache<String, Object> CACHE = CacheBuilder.newBuilder()
            .concurrencyLevel(8)
            .initialCapacity(1024)
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .removalListener(notification -> log.info("remove: key: {}, value: {}", notification.getKey(), notification.getValue()))
            .build();

    /**
     * 缓存
     *
     * @param key   key
     * @param value value
     */
    public static void put(String key, Object value) {
        CACHE.put(key, value);
    }


    public static void remove(String key) {
        if (key == null) {
            return;
        }

        CACHE.invalidate(key);
    }

    public static Object getObject(String key) {
        return CACHE.getIfPresent(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Object value = getObject(key);
        if (value == null) {
            return null;
        }

        return (T) value;
    }
}

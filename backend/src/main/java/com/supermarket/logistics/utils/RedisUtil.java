package com.supermarket.logistics.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 
 * 作用：封装Redis常用操作
 * 
 * 本工具类用途：Token黑名单存储
 * - 用户退出登录时，调用set()将Token存入Redis
 * - 请求验证时，调用hasKey()检查Token是否在黑名单中
 * - 黑名单有效期与Token有效期一致，过期自动清除
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** 设置缓存 */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /** 设置缓存并指定过期时间 */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /** 获取缓存 */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /** 删除缓存 */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /** 批量删除缓存 */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /** 判断key是否存在（用于检查Token是否在黑名单中） */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /** 设置过期时间 */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /** 获取过期时间 */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /** 自增操作 */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /** 自减操作 */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}

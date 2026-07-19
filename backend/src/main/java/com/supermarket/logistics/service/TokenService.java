package com.supermarket.logistics.service;

import com.supermarket.logistics.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token服务类
 * 
 * 作用：管理Token黑名单
 * 
 * 为什么需要Token黑名单？
 * - JWT Token一旦签发，在过期前（默认24小时）都是有效的
 * - 用户退出登录后，Token理论上还能使用，存在安全隐患
 * - 通过Redis存储黑名单，可以让Token在退出时立即失效
 * 
 * 工作流程：
 * 1. 用户退出登录 → addToBlacklist() → Token存入Redis
 * 2. 请求验证时 → isBlacklisted() → 检查Token是否在黑名单
 * 3. Token过期后 → 自动从Redis清除（有效期与Token一致）
 */
@Service
public class TokenService {

    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.expiration:86400000}") // 默认24小时
    private Long tokenExpiration;

    /**
     * 将Token加入黑名单
     * 用户退出登录时调用此方法
     */
    public void addToBlacklist(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        String key = TOKEN_BLACKLIST_PREFIX + token;
        // 存入Redis，黑名单有效期与Token有效期一致，Token过期后自动从黑名单移除
        redisUtil.set(key, "1", tokenExpiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查Token是否在黑名单中
     * 请求验证时调用此方法
     * @return true-已失效，false-有效
     */
    public boolean isBlacklisted(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        String key = TOKEN_BLACKLIST_PREFIX + token;
        return redisUtil.hasKey(key);
    }

    /**
     * 从黑名单移除Token（一般不需要，Token过期自动移除）
     */
    public void removeFromBlacklist(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        String key = TOKEN_BLACKLIST_PREFIX + token;
        redisUtil.delete(key);
    }
}

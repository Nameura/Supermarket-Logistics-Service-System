package com.supermarket.logistics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 主要功能：解决前后端跨域问题
 * 跨域问题说明：
 * 前端运行在 localhost:9000，后端运行在 localhost:8000
 * 由于端口不同，属于不同源，浏览器会阻止跨域请求
 * 这个配置类告诉后端允许前端跨域访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 配置跨域映射
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")         // 对所有接口生效
                .allowedOriginPatterns("*")           // 允许任何域名的前端访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")                  // 允许任何请求头
                .allowCredentials(true)               // 允许携带Cookie和Authorization头
                .maxAge(3600);                        // 预检请求缓存时间（秒）
    }
}

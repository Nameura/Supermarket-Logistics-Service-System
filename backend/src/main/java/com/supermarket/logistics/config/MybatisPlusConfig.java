package com.supermarket.logistics.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * 
 * 主要功能：配置MyBatis-Plus的分页插件
 * - PaginationInnerInterceptor：MyBatis-Plus内置的分页拦截器
 * - DbType.MYSQL：指定数据库类型为MySQL，插件会根据不同数据库生成对应的分页SQL
 * 
 * 分页原理：
 * 1. 调用service.page(page, wrapper)方法时
 * 2. 分页拦截器会自动在SQL语句末尾添加LIMIT子句
 * 3. 同时执行COUNT查询获取总记录数
 * 4. 最终返回包含总记录数和当前页数据的Page对象
 * 
 * 例如：SELECT * FROM goods WHERE deleted = 0
 * 拦截后：SELECT * FROM goods WHERE deleted = 0 LIMIT 0, 10
 */
@Configuration
public class MybatisPlusConfig {
    
    /**
     * 注册MyBatis-Plus拦截器
     * 添加分页插件，使分页查询功能生效
     * @return MybatisPlusInterceptor拦截器对象
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页拦截器，指定数据库类型为MySQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}

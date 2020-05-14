package com.example.configuration;

import com.example.common.MyBatisInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Config
 *
 * @author tianqi
 * @date 2020-4-30
 */
@Configuration
public class MyBatisConfig {
    /**
     * mybatis 自定义拦截器
     */
    @Bean
    public Interceptor getInterceptor() {
        return new MyBatisInterceptor();
    }
}

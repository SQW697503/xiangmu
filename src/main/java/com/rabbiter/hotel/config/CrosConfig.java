package com.rabbiter.hotel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrosConfig implements WebMvcConfigurer{

    /**
     * 配置跨域请求
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 映射所有请求路径
                .allowedOriginPatterns("*") // 允许跨域请求的源地址，设置为 * 表示任意来源都可以
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") // 允许跨域请求的 HTTP 方法
                .allowCredentials(true) // 是否允许发送 Cookie
                .maxAge(3600) // 缓存时间（秒），在此期间相同的跨域请求不再检查
                .allowedHeaders("*"); // 允许跨域请求的请求头
    }
}

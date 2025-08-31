package com.live2d.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置静态资源映射和CORS
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${live2d.model.path:model}")
    private String modelPath;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 为所有路径添加CORS支持
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射模型文件访问路径 - 支持相对路径访问
        registry.addResourceHandler("/api/" + modelPath + "/**")
                .addResourceLocations("file:" + modelPath + "/")
                .setCachePeriod(3600); // 缓存1小时
        
        // 映射模型文件访问路径 - 支持直接访问model路径
        registry.addResourceHandler("/" + modelPath + "/**")
                .addResourceLocations("file:" + modelPath + "/")
                .setCachePeriod(3600); // 缓存1小时
    }
}
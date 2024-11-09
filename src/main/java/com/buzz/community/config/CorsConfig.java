package com.buzz.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해 CORS 적용
                // localhost:3000 → 프론트  localhost:80 → 서버
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용 할 HTTP 메소드
                .allowedHeaders("*") // 모든 Header 허용
                .exposedHeaders("Authorization");
    }




}

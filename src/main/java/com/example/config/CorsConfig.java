package com.example.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // 허용할 엔드포인트 패턴
                .allowedOrigins("http://localhost:3000")  // 허용할 오리진
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")  // 허용할 헤더
                .allowCredentials(true);  // 인증 정보 허용
    }
}

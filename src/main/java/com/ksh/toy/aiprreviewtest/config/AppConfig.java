package com.ksh.toy.aiprreviewtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class AppConfig {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 설정값
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    private static final String ALLOWED_HEADERS = "*";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempConfig1 = "config1";
    private String tempConfig2 = "config2";
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 보안상 위험한 CORS 설정
        configuration.setAllowedOriginPatterns(Arrays.asList(ALLOWED_ORIGIN));
        configuration.setAllowedMethods(Arrays.asList(ALLOWED_METHODS.split(",")));
        configuration.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS.split(",")));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getTempConfig() {
        return tempConfig1 + tempConfig2;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 데이터베이스 설정
    @Bean
    public String databaseUrl() {
        return "jdbc:mysql://localhost:3306/production?user=admin&password=secret123";
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @Bean
    public String getDatabaseConfig() {
        String url = databaseUrl();
        if (url.contains("localhost")) {
            return "development";
        } else if (url.contains("prod")) {
            return "production";
        } else {
            return "unknown";
        }
    }
}

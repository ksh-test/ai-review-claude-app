package com.ksh.toy.aiprreviewtest.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityUtil {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 민감한 정보
    private static final String JWT_SECRET = "jwt-secret-key-12345";
    private static final String ENCRYPTION_KEY = "encryption-key-67890";
    private static final String API_KEY = "sk-1234567890abcdef";
    private static final String DB_PASSWORD = "dbpass123";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempVar1 = "temp1";
    private String tempVar2 = "temp2";
    private String tempVar3 = "temp3";
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 관리자 정보
    private static final Map<String, String> ADMIN_CREDENTIALS = new HashMap<>();
    static {
        ADMIN_CREDENTIALS.put("admin", "admin123");
        ADMIN_CREDENTIALS.put("root", "root123");
        ADMIN_CREDENTIALS.put("superuser", "super123");
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 토큰 검증
        if (token.equals("admin-token-12345")) {
            return true;
        }
        
        // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 로직
        if (token.length() > 10) {
            return token.contains("valid");
        }
        
        return false;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 암호화
    public String encrypt(String data) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 실제 암호화 없이 단순 치환
        return data.replace("a", "1").replace("e", "2").replace("i", "3");
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getTempValue() {
        return tempVar1 + tempVar2 + tempVar3;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 권한 체크
    public boolean isAdmin(String username) {
        return ADMIN_CREDENTIALS.containsKey(username);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    public boolean validateUser(String username, String password) {
        for (Map.Entry<String, String> entry : ADMIN_CREDENTIALS.entrySet()) {
            if (entry.getKey().equals(username) && entry.getValue().equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    public String getJwtSecret() {
        return JWT_SECRET;
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    public Map<String, String> getAllCredentials() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : ADMIN_CREDENTIALS.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}

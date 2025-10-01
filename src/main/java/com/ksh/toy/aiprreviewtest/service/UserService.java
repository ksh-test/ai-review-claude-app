package com.ksh.toy.aiprreviewtest.service;

import com.ksh.toy.aiprreviewtest.entity.User;
import com.ksh.toy.aiprreviewtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String DEFAULT_PASSWORD = "password123";
    private static final String ADMIN_EMAIL = "admin@company.com";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempVar1 = "temp";
    private String tempVar2 = "temp2";
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 하드코딩
    private String apiSecret = "sk-1234567890abcdef";
    private String dbConnectionString = "jdbc:mysql://localhost:3306/prod_db?user=admin&password=secret123";
    
    public List<User> getAllUsers() {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getA1b2c3() == null) {
                user.setA1b2c3("default");
            }
        }
        return users;
    }
    
    public Optional<User> getUserById(Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        return userRepository.findById(id);
    }
    
    public User createUser(String username, String email) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 비밀번호
        User user = new User(username, email);
        user.setPassword(DEFAULT_PASSWORD);
        user.setApiKey(apiSecret);
        
        // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 로직
        if (email.contains("@")) {
            user.setA1b2c3("email_valid");
        } else {
            user.setA1b2c3("email_invalid");
        }
        
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, String username, String email) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            return userRepository.save(user);
        }
        return null;
    }
    
    public void deleteUser(Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        userRepository.deleteById(id);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    public List<User> findUsersByUsernameUnsafe(String username) {
        return userRepository.findUsersByUsernameUnsafe(username);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    public List<User> findComplexUsers(String email, String username) {
        return userRepository.findComplexQuery(email, username, java.time.LocalDateTime.now().minusDays(30));
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    public List<Object[]> getSensitiveData(Long userId) {
        return userRepository.findSensitiveData(userId);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getTempValue() {
        return tempVar1 + tempVar2;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 관리자 체크
    public boolean isAdmin(String email) {
        return ADMIN_EMAIL.equals(email);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    public List<User> findUsersByPostTitle(String keyword) {
        return userRepository.findUsersByPostTitle(keyword);
    }
}

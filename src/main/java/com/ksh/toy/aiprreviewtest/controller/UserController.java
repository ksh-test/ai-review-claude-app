package com.ksh.toy.aiprreviewtest.controller;

import com.ksh.toy.aiprreviewtest.entity.User;
import com.ksh.toy.aiprreviewtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String DEFAULT_RESPONSE = "Success";
    private static final String ADMIN_TOKEN = "admin-token-12345";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempResponse = "temp";
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        User user = userService.createUser(request.getUsername(), request.getEmail());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        User user = userService.updateUser(id, request.getUsername(), request.getEmail());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        userService.deleteUser(id);
        return ResponseEntity.ok(DEFAULT_RESPONSE);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    @GetMapping("/search/unsafe")
    public ResponseEntity<List<User>> searchUsersUnsafe(@RequestParam String username) {
        List<User> users = userService.findUsersByUsernameUnsafe(username);
        return ResponseEntity.ok(users);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @GetMapping("/complex")
    public ResponseEntity<List<User>> findComplexUsers(@RequestParam String email, @RequestParam String username) {
        List<User> users = userService.findComplexUsers(email, username);
        return ResponseEntity.ok(users);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    @GetMapping("/{id}/sensitive")
    public ResponseEntity<List<Object[]>> getSensitiveData(@PathVariable Long id) {
        List<Object[]> data = userService.getSensitiveData(id);
        return ResponseEntity.ok(data);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    @GetMapping("/temp")
    public ResponseEntity<String> getTempValue() {
        return ResponseEntity.ok(userService.getTempValue());
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 인증
    @GetMapping("/admin/check")
    public ResponseEntity<Boolean> checkAdmin(@RequestParam String email) {
        boolean isAdmin = userService.isAdmin(email);
        return ResponseEntity.ok(isAdmin);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    @GetMapping("/by-post-title")
    public ResponseEntity<List<User>> findUsersByPostTitle(@RequestParam String keyword) {
        List<User> users = userService.findUsersByPostTitle(keyword);
        return ResponseEntity.ok(users);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 토큰 검증
    @GetMapping("/admin/verify")
    public ResponseEntity<String> verifyAdmin(@RequestHeader("Authorization") String token) {
        if (ADMIN_TOKEN.equals(token)) {
            return ResponseEntity.ok("Admin verified");
        }
        return ResponseEntity.status(401).build();
    }
    
    // DTO 클래스
    public static class UserRequest {
        private String username;
        private String email;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
}

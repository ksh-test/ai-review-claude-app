package com.ksh.toy.aiprreviewtest.controller;

import com.ksh.toy.aiprreviewtest.entity.Post;
import com.ksh.toy.aiprreviewtest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String DEFAULT_RESPONSE = "Success";
    private static final String ADMIN_TOKEN = "admin-token-12345";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempResponse = "temp";
    
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest request) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Post post = postService.createPost(request.getTitle(), request.getContent(), request.getUserId());
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Post post = postService.updatePost(id, request.getTitle(), request.getContent());
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        postService.deletePost(id);
        return ResponseEntity.ok(DEFAULT_RESPONSE);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    @GetMapping("/search/unsafe")
    public ResponseEntity<List<Post>> searchPostsUnsafe(@RequestParam String title) {
        List<Post> posts = postService.findPostsByTitleUnsafe(title);
        return ResponseEntity.ok(posts);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @GetMapping("/complex")
    public ResponseEntity<List<Post>> findComplexPosts() {
        List<Post> posts = postService.findComplexPosts();
        return ResponseEntity.ok(posts);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    @GetMapping("/{id}/sensitive")
    public ResponseEntity<List<Object[]>> getSensitivePostData(@PathVariable Long id) {
        List<Object[]> data = postService.getSensitivePostData(id);
        return ResponseEntity.ok(data);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    @GetMapping("/temp")
    public ResponseEntity<String> getTempData() {
        return ResponseEntity.ok(postService.getTempData());
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 인증
    @GetMapping("/admin/check")
    public ResponseEntity<Boolean> checkAdmin(@RequestParam String email) {
        boolean isAdmin = postService.isAdminPost(email);
        return ResponseEntity.ok(isAdmin);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    @GetMapping("/by-comment-content")
    public ResponseEntity<List<Post>> findPostsByCommentContent(@RequestParam String keyword) {
        List<Post> posts = postService.findPostsByCommentContent(keyword);
        return ResponseEntity.ok(posts);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 상태 변경
    @PostMapping("/{id}/publish")
    public ResponseEntity<Post> publishPost(@PathVariable Long id) {
        Post post = postService.publishPost(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
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
    public static class PostRequest {
        private String title;
        private String content;
        private Long userId;
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
        
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }
}

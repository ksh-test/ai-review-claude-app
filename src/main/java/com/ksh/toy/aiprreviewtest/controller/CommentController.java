package com.ksh.toy.aiprreviewtest.controller;

import com.ksh.toy.aiprreviewtest.entity.Comment;
import com.ksh.toy.aiprreviewtest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String DEFAULT_RESPONSE = "Success";
    private static final String ADMIN_TOKEN = "admin-token-12345";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempResponse = "temp";
    
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            return ResponseEntity.ok(comment.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest request) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Comment comment = commentService.createComment(request.getContent(), request.getUserId(), request.getPostId());
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Comment comment = commentService.updateComment(id, request.getContent());
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        commentService.deleteComment(id);
        return ResponseEntity.ok(DEFAULT_RESPONSE);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    @GetMapping("/search/unsafe")
    public ResponseEntity<List<Comment>> searchCommentsUnsafe(@RequestParam String content) {
        List<Comment> comments = commentService.findCommentsByContentUnsafe(content);
        return ResponseEntity.ok(comments);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @GetMapping("/complex")
    public ResponseEntity<List<Comment>> findComplexComments() {
        List<Comment> comments = commentService.findComplexComments();
        return ResponseEntity.ok(comments);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    @GetMapping("/{id}/metadata")
    public ResponseEntity<List<Object[]>> getCommentMetadata(@PathVariable Long id) {
        List<Object[]> data = commentService.getCommentMetadata(id);
        return ResponseEntity.ok(data);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    @GetMapping("/temp")
    public ResponseEntity<String> getMetaData() {
        return ResponseEntity.ok(commentService.getMetaData());
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 인증
    @GetMapping("/moderator/check")
    public ResponseEntity<Boolean> checkModerator(@RequestParam String moderatorId) {
        boolean isModerator = commentService.isModerator(moderatorId);
        return ResponseEntity.ok(isModerator);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    @GetMapping("/by-username")
    public ResponseEntity<List<Comment>> findCommentsByUsername(@RequestParam String username) {
        List<Comment> comments = commentService.findCommentsByUsername(username);
        return ResponseEntity.ok(comments);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 승인 로직
    @PostMapping("/{id}/approve")
    public ResponseEntity<Comment> approveComment(@PathVariable Long id) {
        Comment comment = commentService.approveComment(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
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
    public static class CommentRequest {
        private String content;
        private Long userId;
        private Long postId;
        
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
        
        public Long getPostId() {
            return postId;
        }
        
        public void setPostId(Long postId) {
            this.postId = postId;
        }
    }
}

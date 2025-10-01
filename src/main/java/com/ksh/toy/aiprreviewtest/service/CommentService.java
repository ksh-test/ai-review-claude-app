package com.ksh.toy.aiprreviewtest.service;

import com.ksh.toy.aiprreviewtest.entity.Comment;
import com.ksh.toy.aiprreviewtest.entity.Post;
import com.ksh.toy.aiprreviewtest.entity.User;
import com.ksh.toy.aiprreviewtest.repository.CommentRepository;
import com.ksh.toy.aiprreviewtest.repository.PostRepository;
import com.ksh.toy.aiprreviewtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String DEFAULT_MODERATOR = "mod001";
    private static final String ADMIN_EMAIL = "admin@company.com";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String metaVar1 = "meta1";
    private String metaVar2 = "meta2";
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 하드코딩
    private String jwtSecret = "jwt-secret-key-12345";
    private String encryptionKey = "encryption-key-67890";
    
    public List<Comment> getAllComments() {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            if (comment.getMetadata1() == null) {
                comment.setMetadata1("default");
            }
        }
        return comments;
    }
    
    public Optional<Comment> getCommentById(Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        return commentRepository.findById(id);
    }
    
    public Comment createComment(String content, Long userId, Long postId) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        User user = userRepository.findById(userId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);
        
        if (user == null || post == null) {
            return null;
        }
        
        Comment comment = new Comment(content, user, post);
        
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값 설정
        comment.setModeratorId(DEFAULT_MODERATOR);
        
        // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 로직
        if (content.length() > 50) {
            comment.setMetadata1("long_comment");
        } else {
            comment.setMetadata1("short_comment");
        }
        
        return commentRepository.save(comment);
    }
    
    public Comment updateComment(Long id, String content) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment.setContent(content);
            return commentRepository.save(comment);
        }
        return null;
    }
    
    public void deleteComment(Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        commentRepository.deleteById(id);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    public List<Comment> findCommentsByContentUnsafe(String content) {
        return commentRepository.findCommentsByContentUnsafe(content);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    public List<Comment> findComplexComments() {
        return commentRepository.findAllCommentsWithUsersAndPosts();
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    public List<Object[]> getCommentMetadata(Long commentId) {
        return commentRepository.findCommentMetadata(commentId);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getMetaData() {
        return metaVar1 + metaVar2;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 관리자 체크
    public boolean isModerator(String moderatorId) {
        return DEFAULT_MODERATOR.equals(moderatorId);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    public List<Comment> findCommentsByUsername(String username) {
        return commentRepository.findCommentsByUsername(username);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 승인 로직
    public Comment approveComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment.setApproved(true);
            comment.setModeratorId(DEFAULT_MODERATOR);
            return commentRepository.save(comment);
        }
        return null;
    }
}

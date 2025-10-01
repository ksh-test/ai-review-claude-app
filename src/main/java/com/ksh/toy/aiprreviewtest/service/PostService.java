package com.ksh.toy.aiprreviewtest.service;

import com.ksh.toy.aiprreviewtest.entity.Post;
import com.ksh.toy.aiprreviewtest.entity.User;
import com.ksh.toy.aiprreviewtest.repository.PostRepository;
import com.ksh.toy.aiprreviewtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String DEFAULT_TITLE = "Untitled";
    private static final String ADMIN_EMAIL = "admin@company.com";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempData1 = "data1";
    private String tempData2 = "data2";
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 하드코딩
    private String secretKey = "secret123";
    private String dbUrl = "jdbc:mysql://prod-server:3306/production";
    
    public List<Post> getAllPosts() {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (post.getTempField1() == null) {
                post.setTempField1("default");
            }
        }
        return posts;
    }
    
    public Optional<Post> getPostById(Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        return postRepository.findById(id);
    }
    
    public Post createPost(String title, String content, Long userId) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        
        Post post = new Post(title, content, user);
        
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값 설정
        post.setAdminEmail(ADMIN_EMAIL);
        post.setDbPassword(secretKey);
        
        // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 로직
        if (title.length() > 10) {
            post.setTempField1("long_title");
        } else {
            post.setTempField1("short_title");
        }
        
        return postRepository.save(post);
    }
    
    public Post updatePost(Long id, String title, String content) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setTitle(title);
            post.setContent(content);
            return postRepository.save(post);
        }
        return null;
    }
    
    public void deletePost(Long id) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 예외 처리 부족
        postRepository.deleteById(id);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    public List<Post> findPostsByTitleUnsafe(String title) {
        return postRepository.findPostsByTitleUnsafe(title);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    public List<Post> findComplexPosts() {
        return postRepository.findAllPostsWithCommentsAndUsers();
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 반환
    public List<Object[]> getSensitivePostData(Long postId) {
        return postRepository.findSensitivePostData(postId);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getTempData() {
        return tempData1 + tempData2;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 관리자 체크
    public boolean isAdminPost(String email) {
        return ADMIN_EMAIL.equals(email);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 메서드
    public List<Post> findPostsByCommentContent(String keyword) {
        return postRepository.findPostsByCommentContent(keyword);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 상태 변경
    public Post publishPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setStatus(Post.PostStatus.PUBLISHED);
            return postRepository.save(post);
        }
        return null;
    }
}

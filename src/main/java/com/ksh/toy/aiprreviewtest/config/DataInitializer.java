package com.ksh.toy.aiprreviewtest.config;

import com.ksh.toy.aiprreviewtest.entity.User;
import com.ksh.toy.aiprreviewtest.entity.Post;
import com.ksh.toy.aiprreviewtest.entity.Comment;
import com.ksh.toy.aiprreviewtest.repository.UserRepository;
import com.ksh.toy.aiprreviewtest.repository.PostRepository;
import com.ksh.toy.aiprreviewtest.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있는지 확인
        if (userRepository.count() > 0) {
            System.out.println("데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        System.out.println("초기 데이터를 생성합니다...");

        // 샘플 사용자들 생성
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("admin123"); // 코드 리뷰에 검출되기 쉬운 변경사항: 하드코딩된 비밀번호
        admin.setRole("ADMIN");
        admin.setA1b2c3("admin_valid"); // 코드 리뷰에 검출되기 어려운 변경사항: 의미없는 필드 설정
        admin = userRepository.save(admin);

        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123"); // 코드 리뷰에 검출되기 쉬운 변경사항: 하드코딩된 비밀번호
        user1.setRole("USER");
        user1.setA1b2c3("user_valid"); // 코드 리뷰에 검출되기 어려운 변경사항: 의미없는 필드 설정
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("jane_smith");
        user2.setEmail("jane@example.com");
        user2.setPassword("secret456"); // 코드 리뷰에 검출되기 쉬운 변경사항: 하드코딩된 비밀번호
        user2.setRole("USER");
        user2.setA1b2c3("user_valid"); // 코드 리뷰에 검출되기 어려운 변경사항: 의미없는 필드 설정
        user2 = userRepository.save(user2);

        // 샘플 게시글들 생성
        Post post1 = new Post();
        post1.setTitle("Spring Boot 시작하기");
        post1.setContent("Spring Boot는 Java 애플리케이션을 빠르게 개발할 수 있게 해주는 프레임워크입니다.");
        post1.setUser(admin);
        post1 = postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("JPA와 Hibernate 이해하기");
        post2.setContent("JPA는 Java Persistence API의 줄임말로, 객체 관계 매핑을 위한 표준입니다.");
        post2.setUser(user1);
        post2 = postRepository.save(post2);

        Post post3 = new Post();
        post3.setTitle("RESTful API 설계 가이드");
        post3.setContent("RESTful API는 웹 서비스 설계를 위한 아키텍처 스타일입니다.");
        post3.setUser(user2);
        post3 = postRepository.save(post3);

        // 샘플 댓글들 생성
        Comment comment1 = new Comment();
        comment1.setContent("정말 유용한 정보네요! 감사합니다.");
        comment1.setUser(user1);
        comment1.setPost(post1);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("JPA는 정말 강력한 도구입니다.");
        comment2.setUser(user2);
        comment2.setPost(post2);
        commentRepository.save(comment2);

        Comment comment3 = new Comment();
        comment3.setContent("REST API 설계할 때 참고하겠습니다.");
        comment3.setUser(admin);
        comment3.setPost(post3);
        commentRepository.save(comment3);

        Comment comment4 = new Comment();
        comment4.setContent("추가 질문이 있습니다. 언제 답변해주실 수 있나요?");
        comment4.setUser(user1);
        comment4.setPost(post3);
        commentRepository.save(comment4);

        System.out.println("초기 데이터 생성 완료!");
        System.out.println("- 사용자: " + userRepository.count() + "명");
        System.out.println("- 게시글: " + postRepository.count() + "개");
        System.out.println("- 댓글: " + commentRepository.count() + "개");
    }
}

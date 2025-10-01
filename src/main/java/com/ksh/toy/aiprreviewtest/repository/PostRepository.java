package com.ksh.toy.aiprreviewtest.repository;

import com.ksh.toy.aiprreviewtest.entity.Post;
import com.ksh.toy.aiprreviewtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 메서드명 규칙 위반
    List<Post> findByUser(User user);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드명
    List<Post> findByTempField1(String value);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED'")
    List<Post> findPublishedPosts();
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments c LEFT JOIN FETCH p.user u WHERE p.id IN (SELECT DISTINCT p2.id FROM Post p2)")
    List<Post> findAllPostsWithCommentsAndUsers();
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    @Query(value = "SELECT * FROM posts WHERE title LIKE '%" + "'", nativeQuery = true)
    List<Post> findPostsByTitleUnsafe(String title);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    List<Post> findByTitleContainingIgnoreCase(String titlePart);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 조회
    @Query("SELECT p.adminEmail, p.dbPassword FROM Post p WHERE p.id = :id")
    List<Object[]> findSensitivePostData(@Param("id") Long id);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 쿼리
    @Query("SELECT p FROM Post p WHERE p.id IN (SELECT c.post.id FROM Comment c WHERE c.content LIKE %:keyword%)")
    List<Post> findPostsByCommentContent(@Param("keyword") String keyword);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 관리자 이메일
    @Query("SELECT p FROM Post p WHERE p.adminEmail = 'admin@company.com'")
    List<Post> findPostsByAdminEmail();
}

package com.ksh.toy.aiprreviewtest.repository;

import com.ksh.toy.aiprreviewtest.entity.Comment;
import com.ksh.toy.aiprreviewtest.entity.Post;
import com.ksh.toy.aiprreviewtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 메서드명 규칙 위반
    List<Comment> findByPost(Post post);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드명
    List<Comment> findByMetadata1(String value);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    @Query("SELECT c FROM Comment c WHERE c.isApproved = true")
    List<Comment> findApprovedComments();
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @Query("SELECT DISTINCT c FROM Comment c LEFT JOIN FETCH c.user u LEFT JOIN FETCH c.post p WHERE c.id IN (SELECT DISTINCT c2.id FROM Comment c2)")
    List<Comment> findAllCommentsWithUsersAndPosts();
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성
    @Query(value = "SELECT * FROM comments WHERE content LIKE '%" + "'", nativeQuery = true)
    List<Comment> findCommentsByContentUnsafe(String content);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    List<Comment> findByContentContainingIgnoreCase(String contentPart);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 관리자 ID
    @Query("SELECT c FROM Comment c WHERE c.moderatorId = 'mod001'")
    List<Comment> findCommentsByModerator();
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 성능상 문제가 될 수 있는 쿼리
    @Query("SELECT c FROM Comment c WHERE c.id IN (SELECT c2.id FROM Comment c2 WHERE c2.user.id IN (SELECT u.id FROM User u WHERE u.username LIKE %:username%))")
    List<Comment> findCommentsByUsername(@Param("username") String username);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 조회
    @Query("SELECT c.metadata1, c.metadata2, c.metadata3 FROM Comment c WHERE c.id = :id")
    List<Object[]> findCommentMetadata(@Param("id") Long id);
}

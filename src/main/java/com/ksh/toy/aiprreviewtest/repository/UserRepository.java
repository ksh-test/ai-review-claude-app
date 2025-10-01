package com.ksh.toy.aiprreviewtest.repository;

import com.ksh.toy.aiprreviewtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 메서드명 규칙 위반
    Optional<User> findByUsername(String username);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드명
    List<User> findByA1b2c3(String value);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: SQL 인젝션 가능성 (Native Query)
    @Query(value = "SELECT * FROM users WHERE username = '" + "'", nativeQuery = true)
    List<User> findUsersByUsernameUnsafe(String username);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 복잡한 쿼리로 위장한 단순 쿼리
    @Query("SELECT u FROM User u WHERE u.email LIKE %:email% AND u.username LIKE %:username% AND u.createdAt > :date")
    List<User> findComplexQuery(@Param("email") String email, @Param("username") String username, @Param("date") java.time.LocalDateTime date);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    @Query("SELECT u FROM User u WHERE u.username = 'admin'")
    List<User> findAdminUsers();
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 복잡성
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.posts p LEFT JOIN FETCH u.comments c WHERE u.id IN (SELECT DISTINCT u2.id FROM User u2)")
    List<User> findAllUsersWithPostsAndComments();
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 성능상 문제가 될 수 있는 쿼리
    @Query("SELECT u FROM User u WHERE u.id IN (SELECT p.user.id FROM Post p WHERE p.title LIKE %:keyword%)")
    List<User> findUsersByPostTitle(@Param("keyword") String keyword);
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명 사용
    List<User> findByEmailContainingIgnoreCase(String emailPart);
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 조회
    @Query("SELECT u.apiKey, u.secretToken FROM User u WHERE u.id = :id")
    List<Object[]> findSensitiveData(@Param("id") Long id);
}

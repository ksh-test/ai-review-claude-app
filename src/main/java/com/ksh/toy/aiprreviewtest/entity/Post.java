package com.ksh.toy.aiprreviewtest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 외래키 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 컬렉션 초기화
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 상태값
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 필드
    private String tempField1;
    private String tempField2;
    private String tempField3;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 하드코딩
    private String adminEmail = "admin@company.com";
    private String dbPassword = "dbpass123";
    
    public enum PostStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }
    
    // 기본 생성자
    public Post() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 생성자
    public Post(String title, String content, User user) {
        this();
        this.title = title;
        this.content = content;
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public PostStatus getStatus() {
        return status;
    }
    
    public void setStatus(PostStatus status) {
        this.status = status;
    }
    
    public String getTempField1() {
        return tempField1;
    }
    
    public void setTempField1(String tempField1) {
        this.tempField1 = tempField1;
    }
    
    public String getTempField2() {
        return tempField2;
    }
    
    public void setTempField2(String tempField2) {
        this.tempField2 = tempField2;
    }
    
    public String getTempField3() {
        return tempField3;
    }
    
    public void setTempField3(String tempField3) {
        this.tempField3 = tempField3;
    }
    
    public String getAdminEmail() {
        return adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
    
    public String getDbPassword() {
        return dbPassword;
    }
    
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

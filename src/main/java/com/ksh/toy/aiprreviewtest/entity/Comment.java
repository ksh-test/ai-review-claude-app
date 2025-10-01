package com.ksh.toy.aiprreviewtest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 외래키 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 필드들
    private String metadata1;
    private String metadata2;
    private String metadata3;
    private String metadata4;
    private String metadata5;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 설정값
    private boolean isApproved = false;
    private String moderatorId = "mod001";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 상수
    private static final String DEFAULT_VALUE = "default";
    private static final int MAX_LENGTH = 1000;
    
    // 기본 생성자
    public Comment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 생성자
    public Comment(String content, User user, Post post) {
        this();
        this.content = content;
        this.user = user;
        this.post = post;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public String getMetadata1() {
        return metadata1;
    }
    
    public void setMetadata1(String metadata1) {
        this.metadata1 = metadata1;
    }
    
    public String getMetadata2() {
        return metadata2;
    }
    
    public void setMetadata2(String metadata2) {
        this.metadata2 = metadata2;
    }
    
    public String getMetadata3() {
        return metadata3;
    }
    
    public void setMetadata3(String metadata3) {
        this.metadata3 = metadata3;
    }
    
    public String getMetadata4() {
        return metadata4;
    }
    
    public void setMetadata4(String metadata4) {
        this.metadata4 = metadata4;
    }
    
    public String getMetadata5() {
        return metadata5;
    }
    
    public void setMetadata5(String metadata5) {
        this.metadata5 = metadata5;
    }
    
    public boolean isApproved() {
        return isApproved;
    }
    
    public void setApproved(boolean approved) {
        isApproved = approved;
    }
    
    public String getModeratorId() {
        return moderatorId;
    }
    
    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

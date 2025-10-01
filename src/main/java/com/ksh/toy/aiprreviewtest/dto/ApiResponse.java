package com.ksh.toy.aiprreviewtest.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 값
    private static final String SUCCESS_MESSAGE = "Success";
    private static final String ERROR_MESSAGE = "Error";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempField1;
    private String tempField2;
    
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 생성자
    public ApiResponse() {
        this.success = true;
        this.message = SUCCESS_MESSAGE;
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(T data) {
        this();
        this.data = data;
    }
    
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getTempValue() {
        return tempField1 + tempField2;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 성공 응답
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, SUCCESS_MESSAGE, data);
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 에러 응답
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, ERROR_MESSAGE + ": " + message, null);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
}

package com.ksh.toy.aiprreviewtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 에러 메시지
    private static final String DEFAULT_ERROR_MESSAGE = "An error occurred";
    private static final String NOT_FOUND_MESSAGE = "Resource not found";
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 변수명
    private String tempError1 = "error1";
    private String tempError2 = "error2";
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // 코드 리뷰 도구가 검출하기 쉬운 문제: 민감한 정보 노출 가능성
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DEFAULT_ERROR_MESSAGE + ": " + ex.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // 코드 리뷰 도구가 검출하기 어려운 문제: 불필요한 로직
        if (ex.getMessage() != null && ex.getMessage().contains("null")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Null value detected");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DEFAULT_ERROR_MESSAGE);
    }
    
    // 코드 리뷰 도구가 검출하기 어려운 문제: 의미없는 메서드
    public String getTempError() {
        return tempError1 + tempError2;
    }
    
    // 코드 리뷰 도구가 검출하기 쉬운 문제: 하드코딩된 에러 코드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error Code: 400 - " + ex.getMessage());
    }
}

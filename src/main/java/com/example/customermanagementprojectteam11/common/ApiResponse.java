package com.example.customermanagementprojectteam11.common;

import org.springframework.http.HttpStatus;

public class ApiResponse <T> {
    // 속성
    private int status;
    private String message;
    private T data;

    // 생성자
    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    // 기능
    // 공통 성공 응답
    public static <T> ApiResponse<T> success(
            HttpStatus status,
            String message,
            T data) {
        return new ApiResponse<>(status, message, data);
    }

    // 공통 에러 응답
    public static ApiResponse<Void> error(
            HttpStatus status,
            String message) {
        return new ApiResponse<>(status, message, null);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}

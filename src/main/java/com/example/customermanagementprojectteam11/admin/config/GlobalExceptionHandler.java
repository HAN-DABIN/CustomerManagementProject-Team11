package com.example.customermanagementprojectteam11.admin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice //모든 컨트롤러를 예외처리하는 감시자로 응답을 Json형태로 반한함
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)// 어떤 예외를 잡을것인지 정하는 어노테이션
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
            com.example.customermanagementprojectteam11.admin.config.DuplicateEmailException e){ // e <- 예외객체 서비스 로직의 에러메세지를 꺼내쓰기 위해 필요함
        Map<String, Object> response = new LinkedHashMap<>(); // 데이터 넣은 순서 기억하는 지도 // 그냥 HashMap은 순서보장 X
        response.put("status", 409); //
        response.put("message", e.getMessage()); // 메세지를 가져와 응답에 담음
        response.put("error", "Conflict");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 상태코드 409로 설정하고 Json형태로 본문으로 보냄
    }

}

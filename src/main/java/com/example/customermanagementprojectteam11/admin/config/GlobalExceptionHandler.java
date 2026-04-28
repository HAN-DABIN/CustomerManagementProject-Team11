package com.example.customermanagementprojectteam11.admin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice //모든 컨트롤러를 예외처리하는 감시자로 응답을 Json형태로 반한함
public class GlobalExceptionHandler {

    // 이미 사용중인 이메일 입니다
    @ExceptionHandler(DuplicateEmailException.class)// 어떤 예외를 잡을것인지 정하는 어노테이션
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
            com.example.customermanagementprojectteam11.admin.config.DuplicateEmailException e){ // e <- 예외객체 서비스 로직의 에러메세지를 꺼내쓰기 위해 필요함
        Map<String, Object> response = new LinkedHashMap<>(); // 데이터 넣은 순서 기억하는 지도 // 그냥 HashMap은 순서보장 X
        response.put("status", 409); //
        response.put("message", e.getMessage()); // 메세지를 가져와 응답에 담음
        response.put("error", "Conflict");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 상태코드 409로 설정하고 Json형태로 본문으로 보냄
    }
    // 1. 유효성 검사 실패(이름 누락, 비번 길이, 역할 미선택 같은 @Valid 에러)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        // 첫 번째 에러메세지 출력
        response.put("message", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.put("error", "Bad Request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 2. 잘못된 인자 값이 들어온 경우 (전화번호 형식 예외)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("message", ex.getMessage());
        response.put("error", "Bad Request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}

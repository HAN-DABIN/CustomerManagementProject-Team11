package com.example.customermanagementprojectteam11.common;

import com.example.customermanagementprojectteam11.admin.config.DuplicateEmailException;
import com.example.customermanagementprojectteam11.product.handler.AdminException;
import com.example.customermanagementprojectteam11.product.handler.AdminNotFoundException;
import com.example.customermanagementprojectteam11.product.handler.ProductException;
import com.example.customermanagementprojectteam11.product.handler.ProductStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> handleProductException(ProductException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        String message = "입력 형식이 올바르지 않습니다.";

        if(ex.getCause() instanceof IllegalStateException){
            message = ex.getCause().getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(DuplicateEmailException.class)// 어떤 예외를 잡을것인지 정하는 어노테이션
    public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
            DuplicateEmailException e){ // e <- 예외객체 서비스 로직의 에러메세지를 꺼내쓰기 위해 필요함
        Map<String, Object> response = new LinkedHashMap<>(); // 데이터 넣은 순서 기억하는 지도 // 그냥 HashMap은 순서보장 X
        response.put("status", 409); //
        response.put("message", e.getMessage()); // 메세지를 가져와 응답에 담음
        response.put("error", "Conflict");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 상태코드 409로 설정하고 Json형태로 본문으로 보냄
    }


    //  잘못된 인자 값이 들어온 경우 (전화번호 형식 예외)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("message", ex.getMessage());
        response.put("error", "Bad Request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNoValidException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<String> handleAdminNotFoundExceptioin(AdminNotFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(ProductStatusException.class)
    public ResponseEntity<String> handleProductStautsException(ProductStatusException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
}

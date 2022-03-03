package com.example.financial_institution.exception.handler;

import com.example.financial_institution.exception.root.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<String> handleNotFoundException(BaseException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.toString() + "failed");
    }
//
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> accessError() {
//        return ResponseEntity.of(Optional.of("Ошибка авторизации"));
//    }
}
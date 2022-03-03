package ru.otus.spring.bank.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.spring.bank.exception.AccountNotExistException;
import ru.otus.spring.bank.exception.NotFoundTransactionException;
import ru.otus.spring.bank.exception.root.BaseException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<String> handleNotFoundException(BaseException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.toString() + " failed");
    }

    @ExceptionHandler({NotFoundTransactionException.class})
    public ResponseEntity<String> handleNotFoundTransactionException(NotFoundTransactionException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have not make any transaction yet " + ex.toString());
    }
//
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> accessError() {
//        return ResponseEntity.of(Optional.of("Ошибка авторизации"));
//    }
}
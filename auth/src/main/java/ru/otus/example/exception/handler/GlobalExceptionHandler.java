package ru.otus.example.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.example.exception.UserNotFoundException;
import ru.otus.example.exception.root.AuthBaseException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthBaseException.class})
    public ResponseEntity<Object> handleNotFoundException(AuthBaseException ex,
                                                          HttpServletRequest request) {

        Map<String, Object> requestBody = setRequestBody(ex, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestBody);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundTransactionException(UserNotFoundException ex,
                                                                     HttpServletRequest request) {
        Map<String, Object> requestBody = setRequestBody(ex, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestBody);
    }

    //
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> accessError() {
//        return ResponseEntity.of(Optional.of("Ошибка авторизации"));
//    }
    private Map<String, Object> setRequestBody(AuthBaseException exception, HttpServletRequest request) {
        log.debug("Exception message = {}, method = {}, uri = {}",
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("method", request.getMethod());
        body.put("uri", request.getRequestURI());

        return body;
    }
}
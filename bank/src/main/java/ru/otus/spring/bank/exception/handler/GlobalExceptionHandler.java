package ru.otus.spring.bank.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.spring.bank.exception.AccountNotExistException;
import ru.otus.spring.bank.exception.NotFoundTransactionException;
import ru.otus.spring.bank.exception.root.BaseException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<Object> handleNotFoundException(BaseException ex,
                                                          HttpServletRequest request) {

        Map<String, Object> requestBody = setRequestBody(ex, request);
        requestBody.put("exception", ex + "failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestBody);
    }

    @ExceptionHandler({NotFoundTransactionException.class})
    public ResponseEntity<Object> handleNotFoundTransactionException(NotFoundTransactionException ex,
                                                                     HttpServletRequest request) {
        Map<String, Object> requestBody = setRequestBody(ex, request);
        requestBody.put("exception", "You have not make any transaction yet " + ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(requestBody);
    }

    //
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> accessError() {
//        return ResponseEntity.of(Optional.of("Ошибка авторизации"));
//    }
    private Map<String, Object> setRequestBody(BaseException exception, HttpServletRequest request) {
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
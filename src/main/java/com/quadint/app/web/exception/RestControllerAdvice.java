package com.quadint.app.web.exception;

import com.quadint.app.web.controller.response.Response;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity ttoHandler(RuntimeException e) {
        return ResponseEntity.ok().body(Response.error("unknown error"));
    }

    @ExceptionHandler(TtoAppException.class)
    public ResponseEntity ttoHandler(TtoAppException e) {
        return ResponseEntity.ok().body(Response.error(e.getMessage()));
    }

    /* jdbc 에서 하나의 데이터를 예상했지만 데이터가 존재하지 않는 exception handle */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity ttoHandler(EmptyResultDataAccessException e) {
        return ResponseEntity.ok().body(Response.error("unfounded id error"));
    }

    /* @Validation exception handle */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.ok().body(Response.error(errorMessage));
    }
}

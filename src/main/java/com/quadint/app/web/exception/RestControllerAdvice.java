package com.quadint.app.web.exception;

import com.quadint.app.web.controller.response.Response;
import org.springframework.http.ResponseEntity;
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
}

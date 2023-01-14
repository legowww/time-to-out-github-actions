package com.quadint.app.web.exception;

import lombok.Getter;

@Getter
public class TtoAppException extends RuntimeException {
    private String message;

    public TtoAppException(String message) {
        this.message = message;
    }
}

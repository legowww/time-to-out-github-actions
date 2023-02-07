package com.quadint.app.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    String message;
    private T result;

    public static <T> Response<T> success(T result) {
        return new Response("success", result);
    }

    public static Response<Void> success() {
        return new Response("success", null);
    }

    public static Response<Void> error(String message) {
        return new Response(message, null);
    }
}

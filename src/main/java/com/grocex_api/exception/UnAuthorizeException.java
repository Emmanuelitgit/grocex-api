package com.grocex_api.exception;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(String message) {
        super(message);
    }

    public UnAuthorizeException(String message, Throwable cause) {
        super(message, cause);
    }
}

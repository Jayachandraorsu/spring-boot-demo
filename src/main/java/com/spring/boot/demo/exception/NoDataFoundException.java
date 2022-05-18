package com.spring.boot.demo.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String errorMessage) {
        super(errorMessage);
    }

    public NoDataFoundException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}

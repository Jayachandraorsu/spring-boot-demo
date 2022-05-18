package com.spring.boot.demo.exception;

public class NaceDataProcessingException  extends RuntimeException {
    public NaceDataProcessingException(String errorMessage) {
        super(errorMessage);
    }
    public NaceDataProcessingException(String errorMessage,Throwable e) {
        super(errorMessage,e);
    }
}

package com.yh.was.error;

public class RequestMappingError extends Exception {
    public RequestMappingError(String message) {
        super(message);
    }
    public RequestMappingError(String message, Exception ex) {
        super(message, ex);
    }
}
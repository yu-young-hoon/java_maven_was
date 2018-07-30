package com.yh.was.error;

public class RequestFactoryErrorException extends Exception {
    public RequestFactoryErrorException(String message) {
        super(message);
    }
    public RequestFactoryErrorException(String message, Exception ex) {
        super(message, ex);
    }
}

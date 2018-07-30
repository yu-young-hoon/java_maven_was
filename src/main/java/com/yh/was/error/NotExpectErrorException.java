package com.yh.was.error;

public class NotExpectErrorException extends Exception {
    public NotExpectErrorException(String message) {
        super(message);
    }
    public NotExpectErrorException(String message, Exception ex) {
        super(message, ex);
    }
}

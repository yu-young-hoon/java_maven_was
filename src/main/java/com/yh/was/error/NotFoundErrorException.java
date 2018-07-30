package com.yh.was.error;

public class NotFoundErrorException extends  Exception {
    public NotFoundErrorException(String message) {
        super(message);
    }
    public NotFoundErrorException(String message, Exception ex) {
        super(message, ex);
    }
}

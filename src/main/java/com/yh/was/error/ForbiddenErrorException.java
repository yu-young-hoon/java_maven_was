package com.yh.was.error;

public class ForbiddenErrorException extends  Exception {
    public ForbiddenErrorException(String message) {
        super(message);
    }
    public ForbiddenErrorException(String message, Exception ex) {
        super(message, ex);
    }
}

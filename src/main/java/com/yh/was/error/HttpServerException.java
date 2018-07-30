package com.yh.was.error;

public class HttpServerException extends Exception {
    public HttpServerException(String message) {
        super(message);
    }
    public HttpServerException(String message, Exception ex) {
        super(message, ex);
    }
}

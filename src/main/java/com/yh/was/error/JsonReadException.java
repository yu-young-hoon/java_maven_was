package com.yh.was.error;

public class JsonReadException extends Exception {
    public JsonReadException(String message) {
        super(message);
    }
    public JsonReadException(String message, Exception ex) {
        super(message, ex);
    }
}

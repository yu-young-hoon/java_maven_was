package com.yh.was.error;

import com.yh.was.module.HttpResponse;
import com.yh.was.response.ResponseStatus;

public abstract class CustomErrorException extends Exception {
    public CustomErrorException(String message) {
        super(message);
    }
    public CustomErrorException(String message, Exception ex) {
        super(message, ex);
    }
    public abstract ResponseStatus getHttpCode();
}

package com.yh.was.error;

import com.yh.was.module.HttpResponse;
import com.yh.was.response.ResponseStatus;

public class NotFoundErrorException extends CustomErrorException {
    public NotFoundErrorException(String message) {
        super(message);
    }
    public NotFoundErrorException(String message, Exception ex) {
        super(message, ex);
    }

    @Override
    public ResponseStatus getHttpCode() {
        return ResponseStatus.NotFound;
    }
}

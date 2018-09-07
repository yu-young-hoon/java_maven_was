package com.yh.was.error;

import com.yh.was.module.HttpResponse;
import com.yh.was.response.ResponseStatus;

public class ForbiddenErrorException extends CustomErrorException {
    public ForbiddenErrorException(String message) {
        super(message);
    }
    public ForbiddenErrorException(String message, Exception ex) {
        super(message, ex);
    }

    @Override
    public ResponseStatus getHttpCode() {
        return ResponseStatus.Forbidden;
    }
}

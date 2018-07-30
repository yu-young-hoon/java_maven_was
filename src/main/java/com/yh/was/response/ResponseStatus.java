package com.yh.was.response;
/**
 * 전송 코드입니다.
 *
 */
public enum ResponseStatus {
    OK(200, "OK"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    InternalServerError(500, "Internal Server Error");

    private final int code;
    private final String message;
    private ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

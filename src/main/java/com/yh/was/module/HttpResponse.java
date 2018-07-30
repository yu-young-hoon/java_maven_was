package com.yh.was.module;

import com.yh.was.config.Host;
import com.yh.was.response.ResponseStatus;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
/**
 * Response
 * 보낼 정보와 헤더, 메시지 전송을 하였습니다.
 *
 */
public class HttpResponse {
    private Writer writer;
    private StringBuilder message;
    private ResponseStatus status;

    public HttpResponse(Writer writer) {
        this.writer = writer;
        this.message = new StringBuilder();
        this.status = ResponseStatus.OK;
    }
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void addTextLine(String text) {
        message.append(text);
        message.append("\r\n");
    }

    public void sendAll() throws IOException {
        String content = message.toString();
        sendHeader("HTTP/1.0 " + status.getCode() + " " + status.getMessage(), "text/html; charset=utf-8", content.getBytes().length);
        writer.write(content);
        writer.flush();
    }

    private void sendHeader(String responseCode, String contentType, int length) throws IOException {
        writer.write(responseCode + "\r\n");
        Date now = new Date();
        writer.write("Date: " + now + "\r\n");
        writer.write("Server: JHTTP 2.0\r\n");
        writer.write("Content-length: " + length + "\r\n");
        writer.write("Content-type: " + contentType + "\r\n\r\n");
        writer.flush();
    }
}

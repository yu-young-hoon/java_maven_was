package com.yh.was.module;

import com.yh.was.config.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
/**
 * Response 생성 클래스입니다.
 *
 */
public class HttpResponseFactory {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseFactory.class);
    private static HttpResponseFactory instance;
    public synchronized static HttpResponseFactory getInstance() {
        if (instance == null) {
            instance = new HttpResponseFactory();
        }
        return instance;
    }

    public HttpResponse create(Writer witer) {
        HttpResponse response = new HttpResponse(witer);
        return response;
    }
}

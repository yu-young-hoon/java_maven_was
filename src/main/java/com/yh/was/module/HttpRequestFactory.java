package com.yh.was.module;

import com.yh.was.error.NotExpectErrorException;
import com.yh.was.error.RequestFactoryErrorException;
import com.yh.was.launcher.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Request 생성 클래스입니다.
 * request Section, Header Section, Body Section을 해석해서 Request를 반환합니다.
 * request의 path name, parameter를 해석하였습니다.
 * Header는 Hostname만 해석하였고, Body는 해석하지 않았습니다.
 *
 */
public class HttpRequestFactory {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestFactory.class);
    private static HttpRequestFactory instance;
    public synchronized static HttpRequestFactory getInstance() {
        if (instance == null) {
            instance = new HttpRequestFactory();
        }
        return instance;
    }

    public HttpRequest create(Reader in) throws RequestFactoryErrorException {
        HttpRequest request = new HttpRequest();
        StringBuilder line = new StringBuilder();;
        try {
            try {
                while (true) {
                    int c = in.read();
                    if (c == '\n'){
                        c = in.read();
                        if(c =='\r')
                            break;
                        else
                            line.append((char) '\n');
                    }
                    line.append((char) c);
                }
            } catch (IOException ex) {
                // TODO Unhandle
            }

            String[] fulldatas = line.toString().replace("\r\n", "\n").split("\n");
            String requestLine = fulldatas[0];
            if(requestLine.isEmpty()){
                throw new RequestFactoryErrorException("Invalid Request-Line: " + fulldatas[0]);
            }
            StringBuilder headerLine = new StringBuilder();
            StringBuilder bodyLine = new StringBuilder();
            for (int i = 1 ; i < fulldatas.length ; ++i) {
                if(fulldatas[i].isEmpty()){
                    for (int j = i + 1 ; i < fulldatas.length ; ++j) {
                        headerLine.append(fulldatas[j]);
                    }
                    // TODO GET 이외 body data section 처리
                    break;
                }
                int idx = fulldatas[i].indexOf(":");
                if (idx != -1) {
                    if(fulldatas[i].substring(0, idx).toUpperCase().equals("HOST"))
                        request.setRemoteAddress(fulldatas[i].substring(idx + 1, fulldatas[i].length()).split(":")[0].trim());
                    // TODO another Header 처리
                } else {
                    throw new RequestFactoryErrorException("Invalid Request-Line: " + fulldatas[i]);
                }
                headerLine.append(fulldatas[i]);
            }

            String[] tokens = requestLine.split("\\s+");
            String method = tokens[0];

            String[] splitReqParams = tokens[1].split("\\?");
            String requestName = splitReqParams[0].replaceAll("/", "");

            // parameter 처리
            if(splitReqParams.length >= 2){
                String[] params = splitReqParams[1].split("&");
                Arrays.stream(params).forEach(param->{
                    String[] kv = param.split("=");
                    request.addParam(kv[0], kv[1]);
                });
            }

            logger.info("Request name: " + requestName);
            if (method.toUpperCase().equals("GET")) {
                request.setMethod(HttpRequest.METHOD.GET);
            } else if (method.toUpperCase().equals("GET")) {
                request.setMethod(HttpRequest.METHOD.POST);
            } else {
                // TODO annother method
            }
            request.setRequestName(requestName);
        } catch (RequestFactoryErrorException ex) {
            throw new RequestFactoryErrorException("Request create faile", ex);
        }

        return request;
    }
}

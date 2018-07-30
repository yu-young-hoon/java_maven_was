package com.yh.was.module;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request
 * Method, 파라메터, request의 path name을 가지고 있습니다.
 *
 */
public class HttpRequest {
    public enum METHOD {
        GET,
        POST
    }
    private String remoteAddress;
    private Map<String, String> parameters;
    private METHOD method;
    private String requestName;

    public HttpRequest() {
        parameters = new HashMap<String, String>();
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public void setMethod(METHOD method){
        this.method = method;
    }

    public METHOD getMethod() {
        return method;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public void addParam(String key, String value) {
        parameters.put(key, value);
    }
}

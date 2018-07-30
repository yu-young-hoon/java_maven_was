package com.yh.was.config;

import com.yh.was.response.ResponseStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Host 정보입니다.
 * Error 페이지 정보와 호스트, 포트, 도큐먼트 루트 정보를 가지고 있습니다.
 *
 */
public class Host {
    protected class ErrorDocument{
        public int code;
        public String document;
    }
    private String host;
    private String documentRoot;
    private int port;
    private List<ErrorDocument> errorDocument;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Path getDocumentRoot() {
        Path path = Paths.get(documentRoot);
        return path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }

    public String getErrorDocument(ResponseStatus code) {
        Optional<ErrorDocument> optional = errorDocument.stream()
                .filter(x -> x.code == code.getCode())
                .findFirst();
        if(optional.isPresent()) {
            ErrorDocument p = optional.get();
            return p.document;
        }
        return null;
    }
}

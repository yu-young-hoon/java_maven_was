package com.yh.was.interfaces;

import com.yh.was.module.HttpRequest;
import com.yh.was.module.HttpResponse;

/**
 * 서블렛 인터페이스
 *
 */

public interface IServlet {
    abstract public void get(HttpRequest req, HttpResponse res);
    abstract public void post(HttpRequest req, HttpResponse res);
}

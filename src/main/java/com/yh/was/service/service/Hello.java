package com.yh.was.service.service;

import com.yh.was.interfaces.IServlet;
import com.yh.was.module.HttpRequest;
import com.yh.was.module.HttpResponse;
/**
 * /service.hello
 *
 */
public class Hello implements IServlet {
    @Override
    public void get(HttpRequest req, HttpResponse res) {
        res.addTextLine("HELLO");
    }

    @Override
    public void post(HttpRequest req, HttpResponse res) {

    }
}

package com.yh.was.service;

import com.yh.was.interfaces.IServlet;
import com.yh.was.module.HttpRequest;
import com.yh.was.module.HttpResponse;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/**
 * /time
 *
 */
public class Time implements IServlet {
    @Override
    public void get(HttpRequest req, HttpResponse res) {
        ZonedDateTime seoulZone = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분");
        res.addTextLine("<html><div>");
        res.addTextLine(seoulZone.format(format));
        res.addTextLine("</div></html>");
    }

    @Override
    public void post(HttpRequest req, HttpResponse res) {

    }
}

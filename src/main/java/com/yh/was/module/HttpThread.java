package com.yh.was.module;

import com.yh.was.config.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
/**
 * 요청을 받는 역할을 하는 스레드입니다.
 * 요청 생성 에러를 기록하였습니다.
 * 요청을 받아 Request 처리할 Runnable를 생성하여 큐에 넣어주었습니다.
 * port가 같은 host의 경우 같은 스레드에서 accept를 받아 처리합니다.
 *
 */
public class HttpThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(HttpThread.class);
    HttpServer httpServer;
    List<Host> hosts;
    ServerSocket server;

    public HttpThread(List<Host> hosts, HttpServer httpServer) {
        this.httpServer = httpServer;
        this.hosts = hosts;
    }
    public void run(){
        try {
            server = new ServerSocket(hosts.stream().findFirst().get().getPort());
        } catch (IOException ex) {
            logger.error("Http server socket create fail", ex);
        }
        while (true) {
            try {
                Socket con = server.accept();
                logger.info("Accept request");
                Runnable r = new RequestProcessor(con, hosts);
                httpServer.addWork(r);
            } catch (IOException ex) {
                logger.error("Request processor create fail", ex);
            }
        }
    }
}

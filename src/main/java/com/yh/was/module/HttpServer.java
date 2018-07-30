package com.yh.was.module;

import com.yh.was.config.Config;
import com.yh.was.config.ConfigReader;
import com.yh.was.config.Host;
import com.yh.was.error.HttpServerException;
import com.yh.was.launcher.Launcher;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Http Thread 생성과 Request 처리 분배를 처리하였습니다.
 * port가 같은 host의 경우 같은 스레드에서 accept를 받아 처리합니다.
 *
 */
public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private static final int NUM_THREADS = 50;
    private Config config;
    private ExecutorService pool;
    private ArrayDeque<Runnable> workQueue;

    public HttpServer(Config config) {
        this.pool = Executors.newFixedThreadPool(NUM_THREADS);
        this.workQueue = new ArrayDeque<Runnable>();
        this.config = config;
    }

    public void start() throws HttpServerException {
        List<Host> hosts = config.getHosts();
        List<Integer> ports = hosts.stream().map(Host::getPort).distinct().collect(Collectors.toList());
        ports.stream().forEach(i->{
            List<Host> vhosts = hosts.stream().filter(h->h.getPort() == i).collect(Collectors.toList());
            logger.info("Create http thread start: port=" + i);
            HttpThread thread = new HttpThread(vhosts, this);
            thread.start();
        });

        while (true) {
            synchronized (workQueue) {
                if (workQueue.isEmpty()){
                    continue;
                }
                pool.submit(workQueue.pop());
            }
        }
    }

    public void addWork(Runnable r) {
        synchronized (workQueue) {
            workQueue.add(r);
        }
    }
}
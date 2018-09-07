package com.yh.was.launcher;

import com.yh.was.config.Config;
import com.yh.was.config.ConfigReader;
import com.yh.was.error.HttpServerException;
import com.yh.was.error.JsonReadException;
import com.yh.was.module.HttpServer;
import com.yh.was.module.RequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 런처 메인 스레드의 오류를 기록합니다.
 *
 */
public class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        logger.info("info");
        try {
            ConfigReader configReader = new ConfigReader();
            Config config = configReader.readConfigFile("config.json");

            RequestMapper.getInstance().init();

            HttpServer webserver = new HttpServer(config);
            webserver.start();
        } catch (JsonReadException | HttpServerException ex) {
            logger.error("", ex);
        }
    }
}

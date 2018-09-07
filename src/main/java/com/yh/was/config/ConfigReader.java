package com.yh.was.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yh.was.error.JsonReadException;
import com.yh.was.launcher.Launcher;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Json을 읽는 클래스입니다. resources 폴더에 있는 config.json을 읽어와서
 * List<Host>를 갖는 Config를 반환합니다.
 *
 */
public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    public Config readConfigFile(final String configFile) throws JsonReadException {
        Config config = new Config();
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(configFile);
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder sb = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;) {
                sb.append(line);
            }

            Gson gson = new Gson();
            Type type = new TypeToken<List<Host>>(){}.getType();
            List<Host> hostList = gson.fromJson(sb.toString(), type);
            config.setHosts(hostList);
        } catch (Exception ex) {
            throw new JsonReadException("Json parsing error", ex);
        }

        return config;
    }
}

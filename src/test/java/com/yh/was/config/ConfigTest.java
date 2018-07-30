package com.yh.was.config;
import com.yh.was.error.JsonReadException;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class ConfigTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigTest.class);
    Config config = null;

    @Test(expected=JsonReadException.class)
    public void test01() throws  Exception {
        config = new ConfigReader().readConfigFile("test-config01.json");
    }

    @Test(expected=JsonReadException.class)
    public void test02() throws  Exception {
        config = new ConfigReader().readConfigFile("no-file.json");
    }

    @Test
    public void test03() {
        assertNull(config);
    }

    @Test
    public void test04() throws  Exception {
        config = new ConfigReader().readConfigFile("test-config02.json");
        assertNotNull(config);
    }
}

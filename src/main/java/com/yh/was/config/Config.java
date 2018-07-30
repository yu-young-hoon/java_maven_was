package com.yh.was.config;

import java.util.List;

/**
 * config.json의 호스트를 리스트로 가지고 있습니다.
 *
 */
public class Config {
    private List<Host> hosts;

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}

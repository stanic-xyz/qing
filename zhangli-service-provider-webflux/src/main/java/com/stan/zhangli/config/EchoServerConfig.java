package com.stan.zhangli.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 陈云龙
 * @date: 2020/7/7
 * @description
 */
@Configuration
@ConfigurationProperties(prefix = "echoserver")
public class EchoServerConfig {

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

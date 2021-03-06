package com.cdn.configclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能简述：
 *
 * @author caidingnu
 * @create 2019-05-03 01:09
 * @since 1.0.0
 */
@RestController
@RefreshScope
public class TestController {

    @Value("${server.port}")
    String port;

    @Value("${spring.datasource.url}")
    private String dataUrl;

    //每个服务都是一个配置中心客户端
    @RequestMapping("/hi")
    public String hi() {
        return "1";
    }

    @RequestMapping("/")
    public String hello() {
        return port + "\n" + dataUrl;
    }


}

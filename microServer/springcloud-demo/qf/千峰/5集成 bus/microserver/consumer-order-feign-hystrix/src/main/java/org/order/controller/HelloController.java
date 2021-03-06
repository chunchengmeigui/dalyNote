package org.order.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.order.controller.service.FeignOutWebService;
import org.order.controller.service.FeignUserFallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author CDN
 * @desc：
 * @create 2020-03-05 16:57
 * version 1.0.0
 */
@RestController
public class HelloController {

    @Resource
    private RestTemplate restTemplate;

    //    cloud自带的实例，能通过服务名获取服务信息
    @Autowired
    EurekaClient eurekaClient;


    @Autowired
    FeignUserFallbackService feignUserFallbackService;

    @Autowired
    FeignOutWebService feignOutWebService;



    /**
     * desc:    使用ribbon  进行负载均衡（springcloud默认集成了Ribbon）
     * 启动类 @LoadBalanced
     * param:
     * return:
     * author: CDN
     * date: 2020/3/6
     */
    @RequestMapping("ribbon")
    public String ribbon() {
        String object = restTemplate.getForObject("http://PROVIDER-USER/hellouser", String.class);
        return object;
    }


    /**
     * desc: feign 实现负载均衡
     * param:
     * return:
     * author: CDN
     * date: 2020/3/6
     */
    @RequestMapping("feign")
    public String feign() {

        return feignUserFallbackService.getUserHellouser();
    }


    /**
     * desc:   使用地址请求
     * param:
     * return:
     * author: CDN
     * date: 2020/3/7
     */
    @RequestMapping("out")
    public String getOutContent() {
        return feignOutWebService.getContent();
    }


}

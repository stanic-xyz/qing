package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.remote.RemoteHello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);
    private final RemoteHello remoteHello;

    @Value("${test}")
    private String test;

    public HelloController(RemoteHello remoteHello) {
        this.remoteHello = remoteHello;
    }

    @GetMapping("hello")
    public String hello(@RequestParam String name) {

        String value = remoteHello.getUserInfo(name);

        logger.info("来自远程的请求");
        return "this is hello world!" + test + ":" + value;
    }
}

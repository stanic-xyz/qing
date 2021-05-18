package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.remote.BaseService;
import chenyunlong.zhangli.remote.RemoteHello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stan
 */
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    private final RemoteHello remoteHello;

    private final BaseService baseService;

    @Value("${test}")
    private String test;

    public HelloController(RemoteHello remoteHello, BaseService baseService) {
        this.remoteHello = remoteHello;
        this.baseService = baseService;
    }

    @GetMapping("hello")
    public String hello(@RequestParam String name) {

        String value = remoteHello.getUserInfo(name);
        logger.debug("来自远程的请求");
        return "this is hello world!" + test + ":" + value;
    }


    @GetMapping("gschoolInfo")
    public String hello(@RequestParam String appId, String accessToken, String sysIds, String schoolId) {
        String value = baseService.getUserInfo(appId, accessToken, sysIds, schoolId);
        return "this is hello world!" + test + ":" + value;
    }

}

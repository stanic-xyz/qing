package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.remote.RemoteHello;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    private RemoteHello remoteHello;

    @Value("${test}")
    private String test;

    @GetMapping("hello")
    public String hello(@RequestParam String name) {

        String value = remoteHello.getUserList(name);
        if (value != null) {
            return value.toString();
        }
        return "this is hello world!" + test;
    }
}

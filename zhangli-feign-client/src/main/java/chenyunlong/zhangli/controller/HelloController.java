package chenyunlong.zhangli.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @Value("${test}")
    private String test;

    @GetMapping("hello")
    public String hello(@RequestParam String name) {
        return "this is hello world!" + test;
    }
}

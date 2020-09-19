package com.stan.zhangli.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private ITest test;

    @GetMapping("/test")
    public int sayHelloWorld() {
        return test.add(1, 2);
    }
}

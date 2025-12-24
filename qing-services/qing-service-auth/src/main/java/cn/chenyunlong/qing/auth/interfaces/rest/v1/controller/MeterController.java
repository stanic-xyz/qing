package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.qing.auth.interfaces.monitor.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("meter")
@RequiredArgsConstructor
public class MeterController {

    private final MyService myservice;

    @GetMapping("doSomething")
    public ResponseEntity<String> doSomething() {
        myservice.doSomething();
        return ResponseEntity.ok("123");
    }
}

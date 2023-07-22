package cn.chenyunlong.dingtalk.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LeaveServiceTest {


    @Autowired
    private LeaveService leaveService;

    @Test
    void send() {
        String processInstance = leaveService.startProcessInstance();
        System.out.println("processInstance = " + processInstance);
        Assertions.assertNotNull(processInstance);
    }
}

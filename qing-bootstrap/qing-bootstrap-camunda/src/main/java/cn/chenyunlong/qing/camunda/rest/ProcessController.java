package cn.chenyunlong.qing.camunda.rest;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Requ
@RequestMapping("api/process")
public class ProcessController {

    private final RuntimeService runtimeService;

    @PostMapping("create")
    public String createProcess() {
        ProcessInstance testKey = runtimeService.startProcessInstanceByKey("test_key");
        return testKey.getProcessInstanceId();
    }
}

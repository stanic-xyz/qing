package cn.chenyunlong.qing.leave.controller;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deployment")
public class DeploymentController {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;

    public DeploymentController(RuntimeService runtimeService, RepositoryService repositoryService) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
    }

    @PostMapping("/start")
    public Map<String, String> start(@RequestParam("file") MultipartFile file) throws IOException {
        repositoryService.createDeployment()
                .name("leave-approval-test")
                .addInputStream(file.getOriginalFilename(), file.getInputStream())
                .deploy();
        return Map.of("message", "Deployment successful");
    }


    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(2/3);


        List<String> str = new ArrayList<>(List.of("".split(" ")));
    }
}

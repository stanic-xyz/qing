package cn.chenyunlong.qing.leave.controller;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deployments")
public class DeploymentController {

    private final RepositoryService repositoryService;

    public DeploymentController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping
    public Map<String, String> create(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, String> message = new HashMap<>();
        try {
            Deployment deploy = repositoryService.createDeployment()
                    .name("leave-approval-test")
                    .addInputStream(file.getOriginalFilename(), file.getInputStream())
                    .deploy();
            message.put("message", "Deployment successful:version");
            message.put("deploymentId", deploy.getId());
        } catch (Exception e) {
            message.put("message", e.getMessage());
        }
        return message;
    }


    @GetMapping
    public Map<String, String> list() {
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
                .list();
        Map<String, String> message = new HashMap<>(Map.of("message", "list deployment successful:version"));
        deploymentList.forEach(deployment -> message.put(deployment.getId(), deployment.getName()));
        return message;
    }
}

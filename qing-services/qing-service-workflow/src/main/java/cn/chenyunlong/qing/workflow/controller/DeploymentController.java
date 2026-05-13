package cn.chenyunlong.qing.workflow.controller;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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


    /***
     * 查询所有的流程实例
     **/
    @GetMapping
    public Map<String, Object> queryAllDeployedProcesses() {
        Map<String, Object> jsonObjects = new HashMap<>();

        // 查询所有流程定义
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc() // 按流程定义的 Key 排序
                .latestVersion() // 只查询每个流程定义的最新版本
                .orderByProcessDefinitionKey()
                .asc()
                .list();


        // 打印所有已部署的流程的 key 和 name
        for (ProcessDefinition processDefinition : processDefinitions) {
            log.info("Process ID: {}", processDefinition.getId());
            log.info("Process Key: {}", processDefinition.getKey());
            log.info("Process Name: {}", processDefinition.getName());
            log.info("Process Version: {}", processDefinition.getVersion());
            Map<String, Object> object = new HashMap<>();
            object.put("id", processDefinition.getId());
            object.put("key", processDefinition.getKey());
            object.put("name", processDefinition.getName());
            object.put("version", processDefinition.getVersion());
            jsonObjects.put(processDefinition.getId(), object);
        }

        //分页查询
        // 创建查询对象
        //        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
        //                .latestVersion() // 只查询最新版本的流程定义
        //                .orderByProcessDefinitionKey().asc(); // 按流程定义的 Key 升序排序
        //
        //        // 获取总条数
        //        long totalCount = query.count();
        //
        //        // 分页查询流程定义
        //        List<ProcessDefinition> processDefinitions = query.listPage((pageNum - 1) * pageSize, pageSize);

        return jsonObjects;
    }
}

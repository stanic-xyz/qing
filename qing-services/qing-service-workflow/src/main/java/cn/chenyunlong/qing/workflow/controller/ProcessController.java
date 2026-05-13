package cn.chenyunlong.qing.workflow.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
public class ProcessController {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final ProcessEngine processEngine;

    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, @RequestParam String processId) throws Exception {
        // 先尝试查询运行中的流程实例
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        // 如果运行中的实例不存在，尝试查询历史流程实例（已完成的流程）
        if (pi == null) {
            HistoricProcessInstance historicPi = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processId)
                    .singleResult();
            if (historicPi == null) {
                return;
            }
            // 使用历史流程实例信息生成流程图
            generateDiagramForProcess(httpServletResponse, historicPi.getProcessDefinitionId(), processId);
            return;
        }

        // 处理运行中的流程实例
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        if (task == null) {
            // 如果没有任务，但仍然有流程实例，则继续生成
        }

        generateDiagramForProcess(httpServletResponse, pi.getProcessDefinitionId(), pi.getId());
    }

    private void generateDiagramForProcess(HttpServletResponse httpServletResponse, String processDefinitionId, String processInstanceId) throws Exception {
        // 获取正在执行的活动ID（对于运行中的流程）
        List<String> activeActivityIds = new ArrayList<>();
        List<Execution> executions = runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (Execution execution : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(execution.getId());
            activeActivityIds.addAll(ids);
        }

        // 获取已完成的活动ID（用于高亮显示）
        List<String> completedActivityIds = new ArrayList<>();
        List<HistoricActivityInstance> historicActivities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();
        for (HistoricActivityInstance historicActivity : historicActivities) {
            completedActivityIds.add(historicActivity.getActivityId());
        }

        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();

        // 设置响应头
        httpServletResponse.setContentType("image/png");
        //        // 获取当前活跃节点
        //        List<String> activeNodeIds = runtimeService.getActiveActivityIds(processInstanceId);
        //        vo.setActiveNodeIds(activeNodeIds);
        //
        //        // 获取已完成连线
        //        List<HistoricActivityInstance> finished = historyService.createHistoricActivityInstanceQuery()
        //                .processInstanceId(processInstanceId)
        //                .finished()
        //                .list();
        //
        //        List<String> flowIds = finished.stream()
        //                .map(HistoricActivityInstance::getActivityId)
        //                .collect(Collectors.toList());
        //        vo.setCompletedFlowIds(flowIds);

        byte[] buffer = new byte[1024];
        int length;
        // 使用已完成的 activityIds 和空的 flows 生成流程图
        try (InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png",
                activeActivityIds.isEmpty() ? completedActivityIds : activeActivityIds,
                new ArrayList<>(),
                engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(),
                engconf.getClassLoader(), 1.0, true);
             OutputStream out = httpServletResponse.getOutputStream()) {
            while ((length = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
        }
    }
}

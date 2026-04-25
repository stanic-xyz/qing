package cn.chenyunlong.qing.workflow.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;

    @Transactional
    public void withdraw(String processInstanceId, String userId) {
        // 1. 判断是否可撤回
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (instance == null) {
            throw new RuntimeException("流程已结束");
        }
        // 2. 只能发起人撤回
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (!userId.equals(historicProcessInstance.getStartUserId())) {
            throw new RuntimeException("仅发起人可撤回");
        }
        // 3. 终止流程
        runtimeService.deleteProcessInstance(processInstanceId, "用户主动撤销");
    }


    /**
     * 驳回流程
     *
     * @param processInstanceId :
     **/
    @GetMapping("stopFlow")
    @Transactional(rollbackFor = Exception.class)
    public void stopFlow(@RequestParam("id") String processInstanceId) {
        // 在删除前设置拒绝变量
        runtimeService.setVariable(processInstanceId, "refuseFlag", true);

        //拒绝 后一个参数是拒绝的原因
        runtimeService.deleteProcessInstance(processInstanceId, "驳回任务备注原因");
    }

}

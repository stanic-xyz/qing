package cn.chenyunlong.qing.workflow.service;

import cn.chenyunlong.qing.workflow.flowable.config.MyIdmIdentityService;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LeaveProcessServiceTest {

    private ProcessEngine processEngine;
    private LeaveProcessService leaveProcessService;
    private RuntimeService runtimeService;
    private IdentityService identityService;

    @BeforeEach
    void setUp() {
        StandaloneInMemProcessEngineConfiguration config = new StandaloneInMemProcessEngineConfiguration();
        config.setJdbcUrl("jdbc:h2:mem:leave-flow;DB_CLOSE_DELAY=-1");
        config.setJdbcDriver("org.h2.Driver");
        config.setJdbcUsername("sa");
        config.setJdbcPassword("");
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setHistoryLevel(HistoryLevel.FULL);
        MyIdmIdentityService myIdmIdentityService = new MyIdmIdentityService();
        config.setIdentityService(myIdmIdentityService);
        processEngine = config.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .name("leave-approval-test")
                .addClasspathResource("processes/leave-approval.bpmn")
                .deploy();

        TaskService taskService = processEngine.getTaskService();
        runtimeService = processEngine.getRuntimeService();
        identityService = processEngine.getIdentityService();
        leaveProcessService = new LeaveProcessService(runtimeService, taskService, identityService);
    }

    @AfterEach
    void tearDown() {
        if (processEngine != null) {
            processEngine.close();
        }
    }

    @Test
    void happyPathShouldEndAsApproved() {
        ProcessInstance processInstance = startAndSubmit();

        Task supervisorTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertEquals("Task_SupervisorApproval", supervisorTask.getTaskDefinitionKey());
        leaveProcessService.completeSupervisorTask(supervisorTask.getId(), true);

        Task hrTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertEquals("Task_HrApproval", hrTask.getTaskDefinitionKey());
        leaveProcessService.completeHrTask(hrTask.getId(), true);

        Task gmTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertEquals("Task_GmApproval", gmTask.getTaskDefinitionKey());
        leaveProcessService.completeGmTask(gmTask.getId(), true);

        Assertions.assertNull(runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult());
    }

    @Test
    void withdrawShouldTerminateProcessFromBoundaryEvent() {
        ProcessInstance processInstance = startAndSubmit();
        leaveProcessService.withdraw(processInstance.getProcessInstanceId());
        Assertions.assertNull(runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult());
    }

    @Test
    void supervisorApproveShouldMoveToHr() {
        ProcessInstance processInstance = startAndSubmit();
        Task supervisorTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        leaveProcessService.completeSupervisorTask(supervisorTask.getId(), true);

        Task nextTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertNotNull(nextTask);
        Assertions.assertEquals("Task_HrApproval", nextTask.getTaskDefinitionKey());
    }

    @Test
    void supervisorRejectShouldReturnToApplicantRework() {
        ProcessInstance processInstance = startAndSubmit();
        Task supervisorTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        leaveProcessService.completeSupervisorTask(supervisorTask.getId(), false);

        Task reworkTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertNotNull(reworkTask);
        Assertions.assertEquals("Task_ReworkByApplicant", reworkTask.getTaskDefinitionKey());

        leaveProcessService.completeReworkTask(reworkTask.getId());
        Task restartedSupervisorTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertEquals("Task_SupervisorApproval", restartedSupervisorTask.getTaskDefinitionKey());
    }

    private ProcessInstance startAndSubmit() {
        ProcessInstance processInstance = leaveProcessService.startLeaveProcess("zhangsan", 3, "家庭事务", "lisi");
        Task submitTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        Assertions.assertNotNull(submitTask);
        Assertions.assertEquals("Task_SubmitRequest", submitTask.getTaskDefinitionKey());

        leaveProcessService.completeSubmitTask(submitTask.getId());
        return processInstance;
    }
}

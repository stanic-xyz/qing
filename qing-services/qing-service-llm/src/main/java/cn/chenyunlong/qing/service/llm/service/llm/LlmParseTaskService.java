package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.dto.parser.TaskStatusResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LLM 解析异步任务管理服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmParseTaskService {

    private final Map<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();

    /**
     * 提交新任务
     */
    public String submitTask(String type) {
        String taskId = java.util.UUID.randomUUID().toString();
        TaskStatus status = new TaskStatus();
        status.setTaskId(taskId);
        status.setType(type);
        status.setStatus("PENDING");
        status.setProgress(0);
        status.setCreatedAt(LocalDateTime.now());
        status.setUpdatedAt(LocalDateTime.now());

        taskStatusMap.put(taskId, status);
        log.info("Submitted new {} task: {}", type, taskId);
        return taskId;
    }

    /**
     * 更新任务进度
     */
    public void updateProgress(String taskId, int progress) {
        TaskStatus status = taskStatusMap.get(taskId);
        if (status != null) {
            status.setProgress(progress);
            status.setUpdatedAt(LocalDateTime.now());
            if (progress >= 100) {
                status.setStatus("COMPLETED");
            } else if (progress > 0) {
                status.setStatus("RUNNING");
            }
        }
    }

    /**
     * 更新任务状态
     */
    public void updateStatus(String taskId, String status, String errorMessage) {
        TaskStatus taskStatus = taskStatusMap.get(taskId);
        if (taskStatus != null) {
            taskStatus.setStatus(status);
            taskStatus.setErrorMessage(errorMessage);
            taskStatus.setUpdatedAt(LocalDateTime.now());
        }
    }

    /**
     * 获取任务状态
     */
    public TaskStatusResponse getStatus(String taskId) {
        TaskStatus status = taskStatusMap.get(taskId);
        if (status == null) {
            return null;
        }
        return new TaskStatusResponse(status.getTaskId(), status.getStatus(), status.getProgress());
    }

    /**
     * 取消任务
     */
    public boolean cancel(String taskId) {
        TaskStatus status = taskStatusMap.get(taskId);
        if (status != null && "PENDING".equals(status.getStatus())) {
            status.setStatus("CANCELLED");
            status.setUpdatedAt(LocalDateTime.now());
            log.info("Cancelled task: {}", taskId);
            return true;
        }
        return false;
    }

    /**
     * 完成任务
     */
    public void complete(String taskId) {
        TaskStatus status = taskStatusMap.get(taskId);
        if (status != null) {
            status.setStatus("COMPLETED");
            status.setProgress(100);
            status.setUpdatedAt(LocalDateTime.now());
            log.info("Completed task: {}", taskId);
        }
    }

    /**
     * 任务失败
     */
    public void fail(String taskId, String errorMessage) {
        TaskStatus status = taskStatusMap.get(taskId);
        if (status != null) {
            status.setStatus("FAILED");
            status.setErrorMessage(errorMessage);
            status.setUpdatedAt(LocalDateTime.now());
            log.error("Task failed: {} - {}", taskId, errorMessage);
        }
    }

    /**
     * 任务状态内部类
     */
    @Data
    public static class TaskStatus {
        private String taskId;
        private String type;
        private String status;
        private int progress;
        private String errorMessage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}

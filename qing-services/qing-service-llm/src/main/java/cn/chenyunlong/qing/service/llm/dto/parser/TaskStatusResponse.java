package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

/**
 * 异步任务状态响应
 */
@Data
public class TaskStatusResponse {
    private String taskId;
    private String status;
    private int progress;
    private String errorMessage;

    public TaskStatusResponse() {}

    public TaskStatusResponse(String taskId, String status, int progress) {
        this.taskId = taskId;
        this.status = status;
        this.progress = progress;
    }

    public static TaskStatusResponse pending(String taskId) {
        return new TaskStatusResponse(taskId, "PENDING", 0);
    }

    public static TaskStatusResponse running(String taskId, int progress) {
        return new TaskStatusResponse(taskId, "RUNNING", progress);
    }

    public static TaskStatusResponse completed(String taskId) {
        return new TaskStatusResponse(taskId, "COMPLETED", 100);
    }

    public static TaskStatusResponse failed(String taskId, String errorMessage) {
        TaskStatusResponse response = new TaskStatusResponse(taskId, "FAILED", 0);
        response.setErrorMessage(errorMessage);
        return response;
    }
}

package cn.chenyunlong.qing.workflow.model;

public record StartLeaveRequest(String applicant, int leaveDays, String directSupervisor, String reason) {
}

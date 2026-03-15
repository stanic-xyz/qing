package cn.chenyunlong.qing.leave.model;

public record StartLeaveRequest(String applicant, int leaveDays, String reason) {
}

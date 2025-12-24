package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

import lombok.Getter;

/**
 * 认证失败原因枚举
 */
@Getter
public enum AuthFailureReason {
    USER_NOT_FOUND("用户不存在"),
    USER_INACTIVE("用户未激活"),
    USER_SUSPENDED("用户已被暂停"),
    USER_DELETED("用户已删除"),
    ACCOUNT_LOCKED("账户已被锁定"),
    INVALID_CREDENTIALS("用户名或密码错误"),
    PASSWORD_EXPIRED("密码已过期"),
    IP_NOT_ALLOWED("IP地址不允许"),
    LOGIN_TIME_RESTRICTED("登录时间受限"),
    DEVICE_NOT_ALLOWED("设备不允许"),
    SYSTEM_ERROR("系统错误");

    private final String description;

    AuthFailureReason(String description) {
        this.description = description;
    }

}

package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

public enum PasswordExpiredHandlePolicy {

    /**
     * 直接登录失败
     */
    FAIL,
    /**
     * 直接登录失败
     */
    WARNING,

    /**
     * 忽略
     */
    IGNORE
}

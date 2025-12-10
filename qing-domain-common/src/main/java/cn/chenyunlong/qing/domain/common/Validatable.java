package cn.chenyunlong.qing.domain.common;

import cn.chenyunlong.common.constants.ValidStatus;

/**
 * 有效性状态管理接口
 */
public interface Validatable {
    ValidStatus getValidStatus();

    void setValidStatus(ValidStatus status);

    // 状态转换方法
    default void init() {
        setValidStatus(ValidStatus.INITIAL);
    }

    default void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    default void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }

    // 状态检查方法
    default boolean isInitial() {
        return ValidStatus.INITIAL.equals(getValidStatus());
    }

    default boolean isValid() {
        return ValidStatus.VALID.equals(getValidStatus());
    }

    default boolean isInvalid() {
        return ValidStatus.INVALID.equals(getValidStatus());
    }

    // 状态转换验证
    default boolean canBeValid() {
        return !ValidStatus.VALID.equals(getValidStatus());
    }

    default boolean canBeInvalid() {
        return !ValidStatus.INVALID.equals(getValidStatus());
    }
}

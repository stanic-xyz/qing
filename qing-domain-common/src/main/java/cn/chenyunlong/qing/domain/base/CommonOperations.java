package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.qing.domain.base.functions.OperationResult;
import cn.chenyunlong.qing.domain.common.Auditable;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;

// 通用业务操作
public class CommonOperations {

    // 初始化操作
    public static <T extends BaseSimpleBusinessEntity<?>> BusinessOperation<T, T> initialize() {
        return input -> {
            input.init();
            return OperationResult.success(input);
        };
    }

    // 设置为有效状态
    public static <T extends BaseSimpleBusinessEntity<?>> BusinessOperation<T, T> makeValid(String operator) {
        return input -> {
            input.valid(operator);
            return OperationResult.success(input);
        };
    }

    // 设置为无效状态
    public static <T extends BaseSimpleBusinessEntity<?>> BusinessOperation<T, T> makeInvalid(String operator) {
        return input -> {
            input.invalid(operator);
            return OperationResult.success(input);
        };
    }

    // 更新审计信息
    public static <T extends Auditable> BusinessOperation<T, T> updateAudit(String operator) {
        return input -> {
            input.setAuditInfo(input.getAuditInfo().update(operator));
            return OperationResult.success(input);
        };
    }
}

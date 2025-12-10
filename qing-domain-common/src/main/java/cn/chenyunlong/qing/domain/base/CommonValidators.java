package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.common.validator.ValidationResult;
import cn.chenyunlong.qing.domain.common.Auditable;
import cn.chenyunlong.qing.domain.common.Entity;
import cn.chenyunlong.qing.domain.common.Validatable;

import java.util.function.Predicate;

// 通用验证器
public class CommonValidators {

    // 实体存在性验证
    public static <T extends Entity<?>> Validator<T> entityExists() {
        return target -> target.getId() != null ?
                ValidationResult.valid() :
                ValidationResult.invalid("Entity must have an ID");
    }

    // 有效性状态验证
    public static <T extends Validatable> Validator<T> mustBeValid() {
        return target -> target.isValid() ?
                ValidationResult.valid() :
                ValidationResult.invalid("Entity must be in valid state");
    }

    // 审计信息验证
    public static <T extends Auditable> Validator<T> hasAuditInfo() {
        return target -> target.getAuditInfo() != null ?
                ValidationResult.valid() :
                ValidationResult.invalid("Entity must have audit info");
    }

    // 自定义业务规则验证
    public static <T> Validator<T> businessRule(String ruleName, Predicate<T> rule) {
        return target -> rule.test(target) ?
                ValidationResult.valid() :
                ValidationResult.invalid("Business rule violated: " + ruleName);
    }
}

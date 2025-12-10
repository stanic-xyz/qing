package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.common.validator.ValidationResult;

// 验证接口
@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T target);

    default Validator<T> and(Validator<T> other) {
        return target -> {
            ValidationResult first = this.validate(target);
            return first.isValid() ? other.validate(target) : first;
        };
    }
}

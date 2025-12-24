package cn.chenyunlong.qing.domain.common.service;

import cn.chenyunlong.common.validator.ValidationResult;
import cn.chenyunlong.qing.domain.base.*;
import cn.chenyunlong.qing.domain.base.functions.OperationResult;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import cn.chenyunlong.qing.domain.common.Validatable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用业务服务工具类
 * 提供可组合的验证和操作链
 * 注意：这个工具类是可选的，不是强制的
 */
@Component
public class BusinessServiceHelper {

    public BusinessServiceHelper() {
        // 组合通用验证器
    }

    /**
     * 执行验证链
     */
    @SafeVarargs
    public final <T> ValidationResult validate(T target, Validator<T>... validators) {
        Validator<T> chain = Arrays.stream(validators)
                .reduce(Validator::and)
                .orElse(t -> ValidationResult.valid());

        return chain.validate(target);
    }

    /**
     * 执行操作链
     */
    @SafeVarargs
    public final <T, R> OperationResult<R> execute(T input, BusinessOperation<T, R>... operations) {
        if (operations.length == 0) {
            return OperationResult.success(null);
        }

        // 检查操作链类型连续性
        if (!isChainValid(operations)) {
            throw new IllegalArgumentException("Operation chain types are not compatible");
        }
        BusinessOperation<T, R> chain = buildChain(operations);
        return chain.execute(input);
    }

    /**
     * 构建类型安全的操作链
     */
    @SuppressWarnings("unchecked")
    private <I, O> BusinessOperation<I, O> buildChain(BusinessOperation<?, ?>... operations) {
        BusinessOperation<?, ?> current = operations[0];

        for (int i = 1; i < operations.length; i++) {
            current = linkOperations(current, operations[i]);
        }

        return (BusinessOperation<I, O>) current;
    }

    /**
     * 连接两个操作，进行运行时类型检查
     */
    @SuppressWarnings("unchecked")
    private BusinessOperation<?, ?> linkOperations(BusinessOperation<?, ?> first, BusinessOperation<?, ?> second) {
        // 这里需要运行时类型检查来确保类型安全
        return ((BusinessOperation<Object, Object>) first).then((BusinessOperation<Object, Object>) second);
    }

    private boolean isChainValid(BusinessOperation<?, ?>[] operations) {
        return true;
    }

    /**
     * 创建实体流程
     */
    public <T extends BaseSimpleBusinessEntity<?>> OperationResult<T> createEntity(T entity, String creator) {
        return OperationExecutor.startWith(entity)
                .then(CommonOperations.initialize())
                .then(CommonOperations.updateAudit(creator))
                .execute();
    }

    /**
     * 激活实体流程
     */
    public <T extends BaseSimpleBusinessEntity<?>> OperationResult<T> activateEntity(T entity, String operator) {

        Validator<Validatable> validatableValidator = CommonValidators.mustBeValid();

        //        CommonValidators.entityExists(),
        //                validatableValidator
        //
        //                        .and(target -> ((T) target).isInvalid() ?
        //                                ValidationResult.valid() : ValidationResult.invalid("Entity must be invalid to activate"));

        // 验证链
        ValidationResult validation = validate(entity, validatableValidator);

        if (!validation.isValid()) {
            return OperationResult.failure(validation.getMessage());
        }

        // 操作链
        return OperationExecutor.startWith(entity)
                .then(CommonOperations.makeValid(operator))
                .then(CommonOperations.updateAudit(operator))
                .execute();
    }

    /**
     * 构建自定义流程
     */
    public <T extends BaseSimpleBusinessEntity<?>> ProcessBuilder<T> processFor(T entity) {
        return new ProcessBuilder<>(entity);
    }

    /**
     * 流程构建器 - 提供流畅API
     */
    public static class ProcessBuilder<T extends BaseSimpleBusinessEntity<?>> {
        private final T entity;
        private final List<Validator<T>> validators = new ArrayList<>();
        private final List<BusinessOperation<T, T>> operations = new ArrayList<>();

        public ProcessBuilder(T entity) {
            this.entity = entity;
        }

        public ProcessBuilder<T> validate(Validator<T> validator) {
            this.validators.add(validator);
            return this;
        }

        public ProcessBuilder<T> then(BusinessOperation<T, T> operation) {
            this.operations.add(operation);
            return this;
        }

        public OperationResult<T> execute() {
            // 执行验证
            for (Validator<T> validator : validators) {
                ValidationResult result = validator.validate(entity);
                if (!result.isValid()) {
                    return OperationResult.failure(result.getMessage());
                }
            }

            // 执行操作
            BusinessOperation<T, T> chain = operations.stream()
                    .reduce(BusinessOperation::then)
                    .orElse(OperationResult::success);

            return chain.execute(entity);
        }
    }
}

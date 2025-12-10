package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.qing.domain.base.functions.TransitionResult;

import java.util.function.Predicate;

// 状态转换接口
public interface StateTransition<T> {
    TransitionResult execute(T target, String operator);

    default StateTransition<T> withPrecondition(Predicate<T> condition) {
        return (target, operator) -> {
            if (!condition.test(target)) {
                return TransitionResult.failure("Precondition not met");
            }
            return execute(target, operator);
        };
    }
}

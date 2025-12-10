package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.qing.domain.base.functions.OperationResult;

// 业务操作接口
public interface BusinessOperation<T, R> {
    OperationResult<R> execute(T input);

    // 修正后的 then 方法
    default <X> BusinessOperation<T, X> then(BusinessOperation<R, X> next) {
        return input -> {
            OperationResult<R> result = BusinessOperation.this.execute(input);
            return result.isSuccess() ?
                    next.execute(result.result()) :
                    OperationResult.failure(result.error());
        };
    }

    // 转换链式：改变输入和输出类型
    default <X, Y> BusinessOperation<X, Y> compose(
            BusinessOperation<X, T> before,
            BusinessOperation<R, Y> after) {
        return input -> {
            OperationResult<T> firstResult = before.execute(input);
            if (!firstResult.isSuccess()) {
                return OperationResult.failure(firstResult.error());
            }
            OperationResult<R> secondResult = this.execute(firstResult.result());
            return secondResult.isSuccess() ?
                    after.execute(secondResult.result()) :
                    OperationResult.failure(secondResult.error());
        };
    }
}

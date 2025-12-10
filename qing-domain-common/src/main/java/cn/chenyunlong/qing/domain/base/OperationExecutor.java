package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.qing.domain.base.functions.OperationResult;

public class OperationExecutor {

    /**
     * 类型安全的操作链执行器
     */
    public static class ChainBuilder<T> {
        private final T input;
        private BusinessOperation<?, ?> currentOperation;

        private ChainBuilder(T input) {
            this.input = input;
        }

        @SuppressWarnings("unchecked")
        public <R> OperationResult<R> execute() {
            if (currentOperation == null) {
                throw new IllegalStateException("No operations defined in chain");
            }
            return ((BusinessOperation<T, R>) currentOperation).execute(input);
        }
    }

    /**
     * 开始构建操作链
     */
    public static <T> FirstStep<T> startWith(T input) {
        return new FirstStep<>(input);
    }

    public static class FirstStep<T> {
        private final T input;

        private FirstStep(T input) {
            this.input = input;
        }

        public <R> MiddleStep<T, R> then(BusinessOperation<T, R> operation) {
            return new MiddleStep<>(input, operation);
        }
    }

    public static class MiddleStep<T, R> {
        private final T input;
        private final BusinessOperation<T, R> operation;

        private MiddleStep(T input, BusinessOperation<T, R> operation) {
            this.input = input;
            this.operation = operation;
        }

        public <X> MiddleStep<T, X> then(BusinessOperation<R, X> next) {
            BusinessOperation<T, X> chained = operation.then(next);
            return new MiddleStep<>(input, chained);
        }

        public OperationResult<R> execute() {
            return operation.execute(input);
        }
    }
}

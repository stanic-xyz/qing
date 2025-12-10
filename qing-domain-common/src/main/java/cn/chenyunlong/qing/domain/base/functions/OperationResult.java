package cn.chenyunlong.qing.domain.base.functions;

// 操作结果
public record OperationResult<T>(boolean success, T result, String error) {
    public static <T> OperationResult<T> success(T result) {
        return new OperationResult<>(true, result, null);
    }


    public static <T> OperationResult<T> failure(String error) {
        return new OperationResult<>(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }
}

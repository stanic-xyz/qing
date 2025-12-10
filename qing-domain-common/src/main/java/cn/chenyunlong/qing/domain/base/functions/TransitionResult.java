package cn.chenyunlong.qing.domain.base.functions;

// 状态转换结果
public record TransitionResult(boolean success, String error) {
    public static TransitionResult failure(String preconditionNotMet) {
        return new TransitionResult(false, preconditionNotMet);
    }
}

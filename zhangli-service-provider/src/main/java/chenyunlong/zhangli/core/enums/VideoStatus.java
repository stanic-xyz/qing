package chenyunlong.zhangli.core.enums;

/**
 * 视频的状态
 */
public enum VideoStatus implements ValueEnum<Integer> {
    /**
     * 正常
     */
    NORMAL(0),
    /**
     * 被禁用
     */
    DISABLED(1);

    private final Integer value;

    VideoStatus(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

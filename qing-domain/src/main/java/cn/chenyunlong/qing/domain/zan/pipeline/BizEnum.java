package cn.chenyunlong.qing.domain.zan.pipeline;

import cn.chenyunlong.common.constants.BaseEnum;

public enum BizEnum implements BaseEnum<String> {
    LIKE("LIKE", "点赞");

    private final String bizCode;
    private final String bizName;

    BizEnum(String bizCode, String bizName) {
        this.bizCode = bizCode;
        this.bizName = bizName;
    }

    @Override
    public String getValue() {
        return bizCode;
    }

    @Override
    public String getName() {
        return bizName;
    }
}

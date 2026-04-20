package cn.chenyunlong.qing.service.llm.enums;

import cn.chenyunlong.common.enums.CommonEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransactionStatusEnum implements CommonEnum {
    SUCCESS(1, "成功"),
    PENDING(2, "处理中"),
    FAILED(3, "失败");

    private final int code;
    private final String description;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
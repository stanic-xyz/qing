package cn.chenyunlong.qing.service.llm.enums;

import cn.chenyunlong.common.enums.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 操作状态：DRAFT (草稿), PUBLISHED (已发布)
@RequiredArgsConstructor
public enum ConfigStatusEnum implements CommonEnum {
    DRAFT(1, "草稿"),
    PUBLISHED(2, "已发布");

    private final int code;

    @Getter
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

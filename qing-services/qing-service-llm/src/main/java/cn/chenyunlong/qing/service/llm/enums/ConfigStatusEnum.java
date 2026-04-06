package cn.chenyunlong.qing.service.llm.enums;

import lombok.Getter;

// 操作状态：DRAFT (草稿), PUBLISHED (已发布)
public enum ConfigStatusEnum {
    DRAFT("草稿"),
    PUBLISHED("已发布");

    @Getter
    private final String desc;

    ConfigStatusEnum(String desc) {
        this.desc = desc;
    }
}

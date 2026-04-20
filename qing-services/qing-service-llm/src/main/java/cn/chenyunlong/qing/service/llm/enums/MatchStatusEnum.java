package cn.chenyunlong.qing.service.llm.enums;

import cn.chenyunlong.common.enums.CommonEnum;
import lombok.RequiredArgsConstructor;

/**
 * matchStatus: {
 * ORIGINAL: '原始数据，未匹配',
 * AUTO_MATCHED: '自动精确匹配（正则完全命中提取出关键信息）',
 * INTERNAL_TRANSFER: '自动识别为内部转账',
 * MANUAL_EDITED: '手动修改过',
 * SUSPICIOUS: '存疑（模糊匹配或可能出错）'
 * }
 */
@RequiredArgsConstructor
public enum MatchStatusEnum implements CommonEnum {
    ORIGINAL(1, "原始数据"),
    AUTO_MATCHED(2, "自动精确匹配"),
    INTERNAL_TRANSFER(3, "内部转账"),
    MANUAL_EDITED(4, "手动修改"),
    SUSPICIOUS(5, "存疑");

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

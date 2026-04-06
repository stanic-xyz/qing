package cn.chenyunlong.qing.service.llm.enums;

/**
 * matchStatus: {
 * ORIGINAL: '原始数据，未匹配',
 * AUTO_MATCHED: '自动精确匹配（正则完全命中提取出关键信息）',
 * INTERNAL_TRANSFER: '自动识别为内部转账',
 * MANUAL_EDITED: '手动修改过',
 * SUSPICIOUS: '存疑（模糊匹配或可能出错）'
 * }
 */
public enum MatchStatusEnum {
    ORIGINAL,          // 原始数据，未匹配
    AUTO_MATCHED,      // 自动精确匹配（正则完全命中提取出关键信息）
    INTERNAL_TRANSFER, // 自动识别为内部转账
    MANUAL_EDITED,     // 手动修改过
    SUSPICIOUS         // 存疑（模糊匹配或可能出错）
}

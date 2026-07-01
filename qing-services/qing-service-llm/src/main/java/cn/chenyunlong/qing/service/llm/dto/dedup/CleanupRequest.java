package cn.chenyunlong.qing.service.llm.dto.dedup;

import lombok.Data;

import java.util.List;

/**
 * PRIMARY 记录清理请求。
 * <p>
 * 预览模式：仅扫描不标记，返回 DedupReport。
 * 执行模式：对指定记录执行软删除。
 * </p>
 */
@Data
public class CleanupRequest {

    /** 时间容忍分钟数，仅预览模式生效 */
    private int timeToleranceMinutes = 5;

    /** 是否匹配商户，仅预览模式生效 */
    private boolean matchMerchant = true;

    /** 预览模式 — 只扫描不标记 */
    private boolean preview;

    /** 执行模式 — 要软删除的 duplicate 记录 ID 列表 */
    private List<Long> duplicateRecordIds;
}

package cn.chenyunlong.qing.service.llm.dto.dedup;

import lombok.Data;

/**
 * 去重匹配规则配置。
 * <p>
 * 所有字段使用包装类型，{@code null} 表示"未传，使用系统配置默认值"。
 * 系统配置键：
 * <ul>
 *   <li>{@code dedup.timeToleranceMinutes}</li>
 *   <li>{@code dedup.matchMerchant}</li>
 *   <li>{@code dedup.matchDetail}</li>
 * </ul>
 * </p>
 */
@Data
public class DedupConfig {

    /**
     * 时间容忍分钟数。{@code null} 时使用系统配置 {@code dedup.timeToleranceMinutes}。
     */
    private Integer timeToleranceMinutes;

    /**
     * 是否匹配商户字段。{@code null} 时使用系统配置 {@code dedup.matchMerchant}。
     */
    private Boolean matchMerchant;

    /**
     * 是否匹配详情字段。{@code null} 时使用系统配置 {@code dedup.matchDetail}。
     */
    private Boolean matchDetail;
}

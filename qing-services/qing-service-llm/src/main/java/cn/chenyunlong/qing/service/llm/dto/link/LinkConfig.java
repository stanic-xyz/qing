package cn.chenyunlong.qing.service.llm.dto.link;

import lombok.Data;

/**
 * TRACE → PRIMARY 关联匹配配置。
 * <p>
 * 所有字段使用包装类型，{@code null} 表示"未传，使用系统配置默认值"。
 * 系统配置键：
 * <ul>
 *   <li>{@code link.timeToleranceMinutes}</li>
 *   <li>{@code link.matchMerchant}</li>
 * </ul>
 * </p>
 */
@Data
public class LinkConfig {

    /**
     * 时间容忍分钟数。{@code null} 时使用系统配置 {@code link.timeToleranceMinutes}。
     */
    private Integer timeToleranceMinutes;

    /**
     * 是否匹配商户字段。{@code null} 时使用系统配置 {@code link.matchMerchant}。
     */
    private Boolean matchMerchant;
}

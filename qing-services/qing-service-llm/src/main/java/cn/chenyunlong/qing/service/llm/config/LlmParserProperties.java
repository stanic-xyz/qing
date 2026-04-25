package cn.chenyunlong.qing.service.llm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * LLM 解析器配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "llm.parser")
public class LlmParserProperties {

    /**
     * 是否启用缓存
     */
    private boolean cacheEnabled = true;

    /**
     * 缓存过期时间（分钟）
     */
    private int cacheExpireMinutes = 30;

    /**
     * 默认分类策略
     */
    private String defaultStrategy = "BY_CONSUMPTION_TYPE";

    /**
     * 最大文件大小（MB）
     */
    private int maxFileSizeMb = 10;

    /**
     * 解析超时时间（秒）
     */
    private int parseTimeoutSeconds = 60;

    /**
     * 低置信度阈值
     */
    private double lowConfidenceThreshold = 0.7;

    /**
     * 需要审核的置信度阈值
     */
    private double needReviewConfidenceThreshold = 0.9;
}

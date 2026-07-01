package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统通用 KV 配置。
 * 存储去重、关联等功能模块的默认参数，支持运行时修改。
 */
@Entity
@Table(name = "finance_system_config")
@Data
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置键，全局唯一。
     * 命名规范：{module}.{key}，如 dedup.timeToleranceMinutes
     */
    @Column(unique = true, nullable = false, length = 128)
    private String configKey;

    /**
     * 配置值，统一以字符串形式存储。
     */
    @Column(nullable = false, length = 512)
    private String configValue;

    /**
     * 配置说明，便于管理端展示。
     */
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

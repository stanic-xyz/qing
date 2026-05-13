package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * LLM 解析详情变更日志实体
 */
@Entity
@Table(name = "llm_parse_detail_changelog")
@Data
public class LlmParseDetailChangelog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parse_detail_id", nullable = false)
    private Long parseDetailId;

    @Column(name = "field_name", length = 32, nullable = false)
    private String fieldName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "change_source", length = 16, nullable = false)
    private String changeSource;

    @Column(name = "change_reason", length = 256)
    private String changeReason;

    @Column(name = "changed_by", length = 64)
    private String changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        changedAt = LocalDateTime.now();
    }
}

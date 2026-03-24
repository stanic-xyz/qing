package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliation_record")
@Data
public class ReconciliationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionIds; // 关联的多个交易ID，逗号分隔
    private String reconciliationType; // DUPLICATE, MISSING, SUSPICIOUS
    private String status; // PENDING, RESOLVED, IGNORED
    private String resolution; // 解决方式
    private String resolvedBy; // AUTO, MANUAL
    private String notes; // 备注

    private LocalDateTime resolvedAt;
    
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

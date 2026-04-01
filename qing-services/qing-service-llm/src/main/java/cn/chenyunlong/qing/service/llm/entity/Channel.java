package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_channel")
@Data
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // 渠道编码 (如 ALIPAY, WECHAT)

    private String name; // 渠道名称
    private String icon; // 渠道图标

    private String status; // 审批状态: DRAFT, PENDING, EFFECTIVE, REJECTED
    private Boolean isEnabled = true; // 是否启用

    @Version
    private Long version; // 乐观锁版本号

    private Boolean isDeleted = false; // 逻辑删除
    private String createdBy; // 操作人

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

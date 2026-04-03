package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "finance_channel_account_rel", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"channelId", "accountId", "isDeleted"})
})
@Data
public class ChannelAccountRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private Long accountId;

    private String status; // 审批状态: DRAFT, PENDING, EFFECTIVE, REJECTED

    @Version
    private Long version;

    private Boolean isDeleted = false;

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

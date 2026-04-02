package cn.chenyunlong.qing.service.llm.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "finance_transaction_matcher")
@Data
public class TransactionMatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 匹配器名称
    private String description; // 规则描述

    // ---- 关联实体 ----
    private String ruleType;      // CHANNEL, MERCHANT, COUNTERPARTY, INTERNAL_TRANSFER, CUSTOM
    private String targetType;    // 绑定的目标类型：COUNTERPARTY, CATEGORY, ACCOUNT，为空表示全局规则
    private Long targetId;        // 绑定的目标实体 ID

    // ---- 核心：积木式条件 AST ----
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode conditionNode; 

    // ---- 核心：执行动作列表 ----
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode actionNode; 

    private Integer priority;     // 优先级，越大越优先
    private Boolean stopOnMatch = true; // 命中后是否阻断后续规则执行
    private Boolean isActive = true;

    // ---- 统计信息 ----
    private Integer matchCount = 0;   // 匹配命中总数
    private Integer successCount = 0; // 成功导入且未被人工修改的数量

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
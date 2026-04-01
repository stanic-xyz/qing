package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_transaction_matcher")
@Data
public class TransactionMatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 匹配器名称，如：高德打车在云闪付的识别

    // ---- 匹配条件 ----
    private String sourceChannel; // 来源渠道限制，为空表示不限制
    private String matchRegex;    // 核心匹配正则，在 remark/counterparty 中寻找

    // ---- 执行动作 (Action) ----
    private String setType;       // 若匹配成功，强制设置交易类型为：EXPENSE/INCOME/TRANSFER。为空则不修改。
    private String targetMerchant; // 若正则中使用了组 (?<merchant>.*)，则取组内值；否则使用此固定值
    private String targetCounterparty; // 同上，目标对手方
    private String targetCategory; // 映射到的分类
    
    // 若判断为转账，提取卡号的正则组名。或在此指定固定的内部卡号/账号
    private String targetAccountKeyword; 

    // ---- 新增的分类与对手关联 ----
    private String ruleType;      // CHANNEL, MERCHANT, COUNTERPARTY, INTERNAL_TRANSFER, CUSTOM
    private Long counterpartyId;  // 关联的交易对手 ID

    private Integer priority;     // 优先级，越大越优先
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

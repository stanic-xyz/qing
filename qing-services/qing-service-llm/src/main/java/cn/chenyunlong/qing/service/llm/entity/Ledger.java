package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.LedgerStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_ledger")
@Data
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ledgerId; // 账户唯一ID

    private String ledgerName; // 账户名称

    private String bankName; // 开户行/发卡机构

    private String icon; // 渠道图标
    private String remark; // 备注名

    private BigDecimal initialBalance; // 期初余额
    private BigDecimal currentBalance; // 当前余额
    private LocalDateTime balanceAsOf; // 余额对应日期

    @Enumerated(EnumType.STRING)
    private LedgerStatusEnum status; // ACTIVE/CLOSED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentBalance == null) {
            currentBalance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

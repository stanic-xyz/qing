package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountId; // 账户唯一ID

    private String accountName; // 账户名称

    @Enumerated(EnumType.STRING)
    private AccountType accountType; // 账户类型 DEBIT/CREDIT/WALLET

    private String bankName; // 开户行/发卡机构

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel; // 渠道标识 (如 ALIPAY, WECHAT, CMB 等)

    private String icon; // 渠道图标
    private String remark; // 备注名
    private String cardNumber; // 卡号(脱敏)

    private LocalDateTime openingDate; // 开户日期

    private BigDecimal initialBalance; // 期初余额
    private BigDecimal currentBalance; // 当前余额
    private LocalDateTime balanceAsOf; // 余额对应日期

    @Enumerated(EnumType.STRING)
    private AccountStatusEnum status; // ACTIVE/CLOSED

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

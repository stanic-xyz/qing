package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.*;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_transaction_record", indexes = {@Index(columnList = "channel_id"), @Index(columnList = "account_id")})
@Data
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime transactionTime;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // 账户信息冗余
    private String accountName; // 冗余

    // 账户类型冗余
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    // 交易类型冗余
    @Enumerated(EnumType.STRING)
    private TrasactionType type; // INCOME/EXPENSE/TRANSFER

    // 交易金额
    private BigDecimal amount;

    // 交易余额
    private BigDecimal balance;

    // 对手方信息
    @ManyToOne
    private Counterparty counterparty;
    // 商家信息
    private String merchant;
    // 交易类别
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    // 交易子类别
    private String subCategory;
    // 交易状态
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
    // 交易费用
    private BigDecimal fee;
    // 原始交易ID
    private String originalId;
    // 原始文件名
    private String sourceFile;
    // 交易备注
    private String remark;
    // 交易标签
    private String tags; // JSON

    // 关联的交易ID
    private Long linkedId;
    // 用于比对的关联ID
    private String linkedGroupId; // 用于比对的关联ID

    // 关联的导入批次ID
    private String uploadId; // 关联的导入批次ID

    // 软删除标记
    private Boolean isDeleted = false; // 软删除标记
    // 修改标记
    private Boolean isModified = false; // 修改标记
    // 是否已正式导入（用于支持分步导入）
    private Boolean isImported = true; // 是否已正式导入（用于支持分步导入）

    // 存储 JSON 格式的原始解析数据
    @Column(columnDefinition = "TEXT")
    private String originalData; // 存储 JSON 格式的原始解析数据

    // 对账状态
    @Enumerated(EnumType.STRING)
    private ReconciliationStatusEnum reconciliationStatus; // PENDING/MATCHED/MANUAL

    // 是否已确认
    private Boolean confirmed;

    // ======== 新增的匹配与转账相关字段 ========
    @Enumerated(EnumType.STRING)
    private MatchStatusEnum matchStatus = MatchStatusEnum.ORIGINAL;

    private Long targetAccountId; // 内部转账时，目标账户的ID
    private String matchRuleName; // 触发此状态的规则名称

    // 资金类型：INTERNAL(内部如余额宝), EXTERNAL(外部如银行卡), SPLIT(混合支付)
    @Enumerated(EnumType.STRING)
    private FundTypeEnum fundType;

    // 资金来源描述：如 "招商银行储蓄卡(1234)"
    private String fundSource;

    // 关联资金源账户：跨账单对账成功后绑定到具体的银行卡账户ID
    private Long fundSourceAccountId;

    @Enumerated(EnumType.STRING)
    private RecordRoleEnum recordRole = RecordRoleEnum.PRIMARY;

    @Enumerated(EnumType.STRING)
    private TransactionRecordTypeEnum transactionRecordType;

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

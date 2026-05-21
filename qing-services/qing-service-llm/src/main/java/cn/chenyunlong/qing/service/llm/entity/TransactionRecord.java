package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.*;
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

    // 所属账户
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // *交易时间
    private LocalDateTime transactionTime;

    // *账单顺序
    private Integer orderNo;

    // *交易金额,带正负
    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    // *出入账类型
    @Enumerated(EnumType.STRING)
    private TransactionDirectionTypeEnum directionType;

    // *交易余额
    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    // *概要信息
    private String summary;

    // *详情,格式：交易类型-->账户(6217003810043300020/陈先生)
    private String detail;

    // *账户信息冗余
    private String accountName; // 冗余

    // *账户类型冗余
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    // 交易类型冗余
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // INCOME/EXPENSE/TRANSFER

    // 内部转账时，目标账户的ID
    private Long targetAccountId;

    // 对手方信息
    @ManyToOne
    private Counterparty counterparty;

    private String counterpartyStr;

    // 商家信息,必须
    private String merchant;

    // 交易类别
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // 交易子类别,冗余字段
    private String subCategory;

    // 交易状态
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;

    // 交易费用
    private BigDecimal fee;

    // 原始交易ID
    private String originalId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "original_detail_id")
    private RecordDetail originalDetail;

    // 交易标签
    private String tags; // JSON

    // 关联的导入批次ID
    private String uploadId; // 关联的导入批次ID

    // 批次号，用于标记同一批次处理的记录
    private String batchNo;

    // 软删除标记
    private Boolean isDeleted = false; // 软删除标记
    // 修改标记
    private Boolean isModified = false; // 修改标记
    // 是否已正式导入（用于支持分步导入）
    private Boolean isImported = true; // 是否已正式导入（用于支持分步导入）

    // 对账状态
    @Enumerated(EnumType.STRING)
    private ReconciliationStatusEnum reconciliationStatus; // PENDING/MATCHED/MANUAL

    // 是否已确认
    private Boolean confirmed;

    // ======== 新增的匹配与转账相关字段 ========
    @Enumerated(EnumType.STRING)
    private MatchStatusEnum matchStatus = MatchStatusEnum.ORIGINAL;

    private String matchRuleName; // 触发此状态的规则名称

    // 资金来源描述：如 "招商银行储蓄卡(1234)"
    private String fundSource;

    // 关联资金源账户：跨账单对账成功后绑定到具体的银行卡账户ID
    private Long fundSourceAccountId;

    @Enumerated(EnumType.STRING)
    private RecordRoleEnum recordRole = RecordRoleEnum.PRIMARY;

    // 主流水标识
    private Long targetPrimaryRecordId;

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

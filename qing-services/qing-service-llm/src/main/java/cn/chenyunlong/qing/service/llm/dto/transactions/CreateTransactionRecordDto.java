package cn.chenyunlong.qing.service.llm.dto.transactions;

import cn.chenyunlong.qing.service.llm.enums.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// ==================== DTO ====================
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CreateTransactionRecordDto {

    @NotNull(message = "渠道ID不能为空")
    private Long channelId;

    @NotNull(message = "账户ID不能为空")
    private Long accountId;

    @NotNull(message = "交易时间不能为空")
    @PastOrPresent(message = "交易时间不能晚于当前时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime transactionTime;

    @NotNull(message = "账单顺序不能为空")
    @Min(value = 1, message = "账单顺序最小为1")
    private Integer orderNo;

    @NotNull(message = "交易金额不能为空")
    @DecimalMin(value = "-999999999999.99", message = "金额超出范围")
    @DecimalMax(value = "999999999999.99", message = "金额超出范围")
    private BigDecimal amount;

    private BigDecimal balance;  // 交易余额，可为空

    private String summary;

    private String detail;

    // 内部转账目标账户ID（当directionType=TRANSFER时建议提供）
    private Long targetAccountId;

    // 对手方ID（与counterpartyStr二选一）
    private Long counterpartyId;

    // 对手方文本（当没有对手方ID时使用）
    private String counterpartyStr;

    @NotBlank(message = "商家信息不能为空")
    private String merchant;

    @NotNull(message = "交易类别ID不能为空")
    private Long categoryId;

    private String subCategory;  // 子类别冗余，若为空则从Category获取

    private TransactionStatusEnum status;  // 默认创建时设为COMPLETED

    private BigDecimal fee;  // 交易费用

    private String originalId;   // 原始交易ID
    private String sourceFile;   // 原始文件名

    private List<String> tags;         // JSON格式标签

    private Long linkedId;       // 关联交易ID
    private String linkedGroupId; // 关联组ID

    private String uploadId;     // 导入批次ID
    private String batchNo;      // 批次号，为空时系统自动生成

    private String originalData; // 原始解析数据JSON

    private ReconciliationStatusEnum reconciliationStatus; // 对账状态，默认PENDING
    private Boolean confirmed;    // 是否已确认，默认false

    private MatchStatusEnum matchStatus;     // 匹配状态，默认ORIGINAL
    private String matchRuleName;            // 匹配规则名称

    private FundTypeEnum fundType;           // 资金类型
    private String fundSource;               // 资金来源描述
    private Long fundSourceAccountId;        // 关联资金源账户ID

    private RecordRoleEnum recordRole;       // 记录角色，默认PRIMARY

    // 业务交易类型（收入/支出/转账）冗余，若为空则根据directionType自动推导
    private TransactionType transactionType;

    // 细分记录类型（如消费、提现等），可为空
    private TransactionRecordTypeEnum transactionRecordType;
}

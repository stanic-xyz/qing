package cn.chenyunlong.qing.service.llm.dto.transactions;

import cn.chenyunlong.qing.service.llm.enums.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 手工新增交易的公开输入模型。
 */
@Data
public class CreateTransactionRecordDto {

    /**
     * 交易所属账户 ID。
     * 必填，服务端会根据该字段加载账户并补齐账户冗余信息。
     */
    @NotNull(message = "账户ID不能为空")
    private Long accountId;

    /**
     * 交易发生时间。
     * 必填，且不能晚于当前时间。
     */
    @NotNull(message = "交易时间不能为空")
    @PastOrPresent(message = "交易时间不能晚于当前时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime transactionTime;

    /**
     * 账单内排序序号。
     * 可选，主要用于保留人工录入或外部来源中的顺序信息。
     */
    @Min(value = 1, message = "账单顺序最小为1")
    private Integer orderNo;

    /**
     * 当前记录对所属账户的收支方向。
     * 必填，仅允许表达收入或支出，不再通过金额正负隐式推导。
     */
    @NotNull(message = "directionType不能为空")
    private TransactionDirectionTypeEnum directionType;

    /**
     * 交易金额。
     * 必填，允许正数或负数，且金额符号必须与 `directionType` 保持一致。
     */
    @NotNull(message = "交易金额不能为空")
    @DecimalMin(value = "-999999999999.99", message = "金额超出范围")
    @DecimalMax(value = "999999999999.99", message = "金额超出范围")
    private BigDecimal amount;

    /**
     * 交易后余额。
     * 可选，未传时不补默认值。
     */
    private BigDecimal balance;

    /**
     * 交易摘要。
     * 可选，适合存放简短的场景描述。
     */
    private String summary;

    /**
     * 交易详情。
     * 可选，适合存放原始备注或更长的描述信息。
     */
    private String detail;

    /**
     * 内部转账目标账户 ID。
     * 当前手工交易接口暂不承载转账语义，传入时会被服务层拒绝并引导改用独立转账接口。
     */
    private Long targetAccountId;

    /**
     * 对手方 ID。
     * 可选，与 `counterpartyStr` 二选一，优先使用 ID 关联实体。
     */
    private Long counterpartyId;

    /**
     * 对手方名称文本。
     * 可选，当没有对手方实体时使用。
     */
    private String counterpartyStr;

    /**
     * 商户名称。
     * 可选，手工录入时允许留空。
     */
    private String merchant;

    /**
     * 分类 ID。
     * 可选，未传时交易可先不分类。
     */
    private Long categoryId;

    /**
     * 分类名称。
     * 可选，当未传 `categoryId` 时可通过名称解析分类；若两者同时传入则必须指向同一分类。
     */
    private String categoryName;

    /**
     * 子分类名称。
     * 可选，通常作为冗余展示字段保留。
     */
    private String subCategory;

    /**
     * 交易状态。
     * 可选，未传时服务端默认写入 `SUCCESS`。
     */
    private TransactionStatusEnum status;

    /**
     * 交易手续费。
     * 可选，若传入则必须为非负数。
     */
    private BigDecimal fee;

    /**
     * 标签列表。
     * 可选，服务端会按当前实现拼接后落库。
     */
    private List<String> tags;

    /**
     * 导入任务 ID。
     * 可选，用于将手工新增记录关联到外部导入上下文。
     */
    private String uploadId;

    /**
     * 批次号。
     * 可选，未传时由服务端自动生成。
     */
    private String batchNo;

    /**
     * 对账状态。
     * 可选，未传时服务端默认写入 `PENDING`。
     */
    private ReconciliationStatusEnum reconciliationStatus;

    /**
     * 是否已确认。
     * 可选，未传时服务端默认写入 `false`。
     */
    private Boolean confirmed;

    /**
     * 匹配状态。
     * 可选，未传时服务端默认写入 `ORIGINAL`。
     */
    private MatchStatusEnum matchStatus;

    /**
     * 匹配规则名称。
     * 可选，仅在上层已有匹配结果时传入。
     */
    private String matchRuleName;

    /**
     * 资金来源描述。
     * 可选，通常用于记录跨账户或跨渠道来源信息。
     */
    private String fundSource;

    /**
     * 资金来源账户 ID。
     * 可选，用于关联已识别的来源账户。
     */
    private Long fundSourceAccountId;

    /**
     * 记录角色。
     * 可选，未传时服务端默认写入 `PRIMARY`。
     */
    private RecordRoleEnum recordRole;

    /**
     * 业务交易类型。
     * 可选，当前手工交易接口仅面向单笔收入/支出记录；`TRANSFER` 语义由后续独立接口承载。
     */
    private TransactionType transactionType;

    /**
     * 细分交易记录类型。
     * 可选，用于补充更细粒度的业务语义。
     */
    private TransactionRecordTypeEnum transactionRecordType;
}

package cn.chenyunlong.qing.service.llm.dto.transactions;

import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

/**
 * 交易更新的公开输入模型。
 * 通过 setter 调用痕迹区分“字段未传”和“显式传 null”。
 */
public class UpdateTransactionRecordDto {

    /**
     * 分类 ID。
     * 可选，未传保持原值；显式传 `null` 表示清空分类。
     */
    private Long categoryId;

    /**
     * 分类名称。
     * 可选，未传保持原值；显式传空白或 `null` 表示清空分类。
     */
    private String categoryName;

    /**
     * 交易金额。
     * 可选，未传保持原值；传入时允许正数或负数，实际符号由服务层与 `directionType` 联合校验。
     */
    @DecimalMin(value = "-999999999999.99", message = "金额超出范围")
    @DecimalMax(value = "999999999999.99", message = "金额超出范围")
    private BigDecimal amount;

    /**
     * 对手方 ID。
     * 可选，与 `counterpartyStr` 共同决定对手方更新语义。
     */
    private Long counterpartyId;

    /**
     * 对手方名称文本。
     * 可选，显式传 `null` 时可清空文本型对手方。
     */
    private String counterpartyStr;

    /**
     * 商户名称。
     * 可选，显式传 `null` 时清空商户。
     */
    private String merchant;

    /**
     * 业务交易类型。
     * 可选，未传保持原值；显式传 `null` 表示清空。
     * 当前手工交易接口只保留收入/支出语义，显式传入 `TRANSFER` 会被服务层拒绝。
     */
    private TransactionType transactionType;

    /**
     * 当前记录对所属账户的收支方向。
     * 可选，未传保持原值；显式传值时必须与金额符号、分类方向约束保持一致。
     */
    private TransactionDirectionTypeEnum directionType;

    @JsonIgnore
    private boolean categoryIdSpecified;

    @JsonIgnore
    private boolean categoryNameSpecified;

    @JsonIgnore
    private boolean amountSpecified;

    @JsonIgnore
    private boolean counterpartyIdSpecified;

    @JsonIgnore
    private boolean counterpartyStrSpecified;

    @JsonIgnore
    private boolean merchantSpecified;

    @JsonIgnore
    private boolean transactionTypeSpecified;

    @JsonIgnore
    private boolean directionTypeSpecified;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryIdSpecified = true;
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryNameSpecified = true;
        this.categoryName = categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amountSpecified = true;
        this.amount = amount;
    }

    public Long getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(Long counterpartyId) {
        this.counterpartyIdSpecified = true;
        this.counterpartyId = counterpartyId;
    }

    public String getCounterpartyStr() {
        return counterpartyStr;
    }

    public void setCounterpartyStr(String counterpartyStr) {
        this.counterpartyStrSpecified = true;
        this.counterpartyStr = counterpartyStr;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchantSpecified = true;
        this.merchant = merchant;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionTypeSpecified = true;
        this.transactionType = transactionType;
    }

    public TransactionDirectionTypeEnum getDirectionType() {
        return directionType;
    }

    public void setDirectionType(TransactionDirectionTypeEnum directionType) {
        this.directionTypeSpecified = true;
        this.directionType = directionType;
    }

    /**
     * 返回分类字段是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isCategoryIdSpecified() {
        return categoryIdSpecified;
    }

    /**
     * 返回分类名称字段是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isCategoryNameSpecified() {
        return categoryNameSpecified;
    }

    /**
     * 返回金额字段是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isAmountSpecified() {
        return amountSpecified;
    }

    /**
     * 返回对手方 ID 是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isCounterpartyIdSpecified() {
        return counterpartyIdSpecified;
    }

    /**
     * 返回对手方文本是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isCounterpartyStrSpecified() {
        return counterpartyStrSpecified;
    }

    /**
     * 返回商户字段是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isMerchantSpecified() {
        return merchantSpecified;
    }

    /**
     * 返回交易类型字段是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isTransactionTypeSpecified() {
        return transactionTypeSpecified;
    }

    /**
     * 返回收支方向字段是否在请求体中显式出现。
     */
    @JsonIgnore
    public boolean isDirectionTypeSpecified() {
        return directionTypeSpecified;
    }
}

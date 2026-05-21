package cn.chenyunlong.qing.service.llm.dto.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountStatisticsDTO {
    private Long accountId;
    private String accountName;
    private Long transactionCount;
    private Long incomeCount;
    private Long expenseCount;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;
}

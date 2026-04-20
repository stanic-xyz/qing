package cn.chenyunlong.qing.service.llm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UploadBatchOverviewResponse {
    private String uploadId;
    private String fileName;
    private long fileSize;
    private Integer totalRecords;
    private Integer incomeCount;
    private Integer expenseCount;
    private Integer transferCount;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private LocalDateTime transactionStartTime;
    private LocalDateTime transactionEndTime;
    private Integer batchCount;
    private String accountName;
}

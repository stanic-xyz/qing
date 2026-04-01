package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PreviewRecordDTO {
    private String tempId;
    private String transactionTime;
    private String channel;
    private String type;
    private BigDecimal amount;
    private String counterparty;
    private String merchant;
    private String status;
    private MatchStatusEnum matchStatus;
    private String matchRuleName;
    private Long targetAccountId;

    public static PreviewRecordDTO fromEntity(TransactionRecord record, String tempId) {
        PreviewRecordDTO dto = new PreviewRecordDTO();
        dto.setTempId(tempId);
        dto.setTransactionTime(record.getTransactionTime() != null ? record.getTransactionTime().toString() : "");
        dto.setChannel(record.getChannel());
        dto.setType(record.getType());
        dto.setAmount(record.getAmount());
        dto.setCounterparty(record.getCounterparty());
        dto.setMerchant(record.getMerchant());
        dto.setStatus(record.getStatus());
        dto.setMatchStatus(record.getMatchStatus());
        dto.setMatchRuleName(record.getMatchRuleName());
        dto.setTargetAccountId(record.getTargetAccountId());
        return dto;
    }
}

package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PreviewRecordDTO {
    private String tempId;
    private LocalDateTime transactionTime;
    private String channel;
    private String type;
    private BigDecimal amount;
    private String counterparty;
    private String merchant;
    private String status;

    public static PreviewRecordDTO fromEntity(TransactionRecord record, String tempId) {
        PreviewRecordDTO dto = new PreviewRecordDTO();
        dto.setTempId(tempId);
        dto.setTransactionTime(record.getTransactionTime());
        dto.setChannel(record.getChannel());
        dto.setType(record.getType());
        dto.setAmount(record.getAmount());
        dto.setCounterparty(record.getCounterparty());
        dto.setMerchant(record.getMerchant());
        dto.setStatus(record.getStatus());
        return dto;
    }
}

package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class PreviewRecordDTO {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    private String fundType;
    private String fundSource;
    private Long fundSourceAccountId;
    private Map<String, Object> extData; // 差异化扩展数据

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
        dto.setFundType(record.getFundType());
        dto.setFundSource(record.getFundSource());
        dto.setFundSourceAccountId(record.getFundSourceAccountId());

        // 解析 originalData JSON 到 extData Map
        if (record.getOriginalData() != null && !record.getOriginalData().isEmpty()) {
            try {
                dto.setExtData(objectMapper.readValue(record.getOriginalData(), Map.class));
            } catch (Exception e) {
                // 解析失败时忽略，或记录日志
            }
        }
        return dto;
    }
}

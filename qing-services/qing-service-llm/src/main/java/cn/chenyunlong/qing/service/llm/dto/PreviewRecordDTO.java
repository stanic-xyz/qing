package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Data
@SuppressWarnings({"unused", "unchecked"})
public class PreviewRecordDTO {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private AccountDTO account;
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
    private String recordRole;

    public static PreviewRecordDTO fromEntity(TransactionRecord record, String tempId) {
        PreviewRecordDTO dto = new PreviewRecordDTO();
        dto.setTempId(tempId);
        dto.setAccount(record.getAccount() != null ? AccountDTO.of(record.getAccount()) : null);
        dto.setTransactionTime(record.getTransactionTime() != null ? record.getTransactionTime().toString() : "");
        Channel recordChannel = record.getChannel();
        dto.setChannel(recordChannel != null ? recordChannel.getName() : null);
        dto.setType(record.getType().name());
        dto.setAmount(record.getAmount());
        dto.setCounterparty(record.getCounterparty() != null ? record.getCounterparty().getName() : null);
        dto.setMerchant(record.getMerchant());
        dto.setStatus(record.getStatus().name());
        dto.setMatchStatus(record.getMatchStatus());
        dto.setMatchRuleName(record.getMatchRuleName());
        dto.setTargetAccountId(record.getTargetAccountId());
        dto.setFundType(record.getFundType() != null ? record.getFundType().name() : null);
        dto.setFundSource(record.getFundSource());
        dto.setFundSourceAccountId(record.getFundSourceAccountId());
        dto.setRecordRole(record.getRecordRole() != null ? record.getRecordRole().name() : "PRIMARY");

        // 解析 originalData JSON 到 extData Map
        if (record.getOriginalData() != null && !record.getOriginalData().isEmpty()) {
            try {
                dto.setExtData(objectMapper.readValue(record.getOriginalData(), Map.class));
            } catch (Exception e) {
                // 解析失败时忽略，或记录日志
                log.error("Failed to parse originalData JSON: {}", record.getOriginalData(), e);
            }
        }
        return dto;
    }
}

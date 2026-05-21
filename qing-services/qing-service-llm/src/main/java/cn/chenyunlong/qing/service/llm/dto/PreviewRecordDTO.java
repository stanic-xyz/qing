package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Data
@SuppressWarnings({"unused"})
public class PreviewRecordDTO {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private AccountDTO account;

    private Long id;

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
        dto.setAccount(record.getAccount() != null ? AccountDTO.of(record.getAccount()) : null);
        dto.setTransactionTime(record.getTransactionTime() != null ? record.getTransactionTime().toString() : "");
        Channel recordChannel = record.getAccount().getChannel();
        dto.setChannel(recordChannel != null ? recordChannel.getName() : null);
        TransactionType transactionType = record.getTransactionType();
        if (transactionType != null) {
            dto.setType(transactionType.name());
        }
        dto.setAmount(record.getAmount());
        dto.setCounterparty(record.getCounterparty() != null ? record.getCounterparty().getName() : null);
        dto.setMerchant(record.getMerchant());
        dto.setStatus(record.getStatus().name());
        dto.setMatchStatus(record.getMatchStatus());
        dto.setMatchRuleName(record.getMatchRuleName());
        dto.setTargetAccountId(record.getTargetAccountId());
        dto.setFundSource(record.getFundSource());
        dto.setFundSourceAccountId(record.getFundSourceAccountId());
        dto.setRecordRole(record.getRecordRole() != null ? record.getRecordRole().name() : "PRIMARY");

        // 预览时不返回 extData（数据量太大，仅详情需要时单独加载）
        dto.setExtData(null);
        return dto;
    }

    public static PreviewRecordDTO fromEntity(UnifiedDraftRecord record) {

        UnifiedDraftBatch unifiedDraftBatch = record.getBatch();

        PreviewRecordDTO dto = new PreviewRecordDTO();

        dto.setId(record.getId());
        Account batchAccount = unifiedDraftBatch.getAccount();
        assert batchAccount != null;

        dto.setAccount(AccountDTO.of(batchAccount));
        dto.setTransactionTime(record.getTransactionTime() != null ? record.getTransactionTime().toString() : "");
        Channel recordChannel = batchAccount.getChannel();
        dto.setChannel(recordChannel != null ? recordChannel.getName() : null);
        dto.setType(record.getDirection().name());
        dto.setAmount(record.getAmount());
        dto.setCounterparty(record.getCounterparty() != null ? record.getCounterparty().getName() : null);
        dto.setMerchant(record.getMerchant());
        // todo 这里暂时不写匹配规则了
        dto.setMatchStatus(MatchStatusEnum.AUTO_MATCHED);

        dto.setTargetAccountId(batchAccount.getId());
        //        dto.setFundType(record.getFundType() != null ? record.getFundType().name() : null);
        //        dto.setFundSource(record.getFundSource());
        //        dto.setFundSourceAccountId(record.getFundSourceAccountId());
        //        dto.setRecordRole(record.getRecordRole() != null ? record.getRecordRole().name() : "PRIMARY");

        // 预览时不返回 extData（数据量太大，仅详情需要时单独加载）
        dto.setExtData(null);
        return dto;
    }
}

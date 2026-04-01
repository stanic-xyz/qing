package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/compare")
@Slf4j
public class TransactionCompareController {

    @Autowired
    private TransactionRecordRepository transactionRepo;

    @GetMapping
    public Result<List<CompareGroupDTO>> getCompareData(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "compareStatus", defaultValue = "MATCHED") String compareStatus,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "accountId", required = false) Long accountId) {

        // 1. 前置基础查询 (Pre-grouping filter)
        Specification<TransactionRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("transactionTime"), start));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("transactionTime"), end));
            }
            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }
            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            
            predicates.add(cb.equal(root.get("isDeleted"), false));
            
            predicates.add(cb.or(
                cb.equal(root.get("isImported"), true),
                cb.isNull(root.get("isImported"))
            ));
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<TransactionRecord> allRecords = transactionRepo.findAll(spec);

        // 2. 内存聚合分组 (Grouping)
        // 简单的聚合逻辑：按金额 + 类型 + 日期(忽略时分秒) 分组
        Map<String, List<TransactionRecord>> grouped = new HashMap<>();
        for (TransactionRecord r : allRecords) {
            if (r.getTransactionTime() == null || r.getAmount() == null) continue;
            String key = r.getAmount().toPlainString() + "_" + r.getType() + "_" + r.getTransactionTime().toLocalDate().toString();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        // 3. 后置组过滤 (Post-grouping filter)
        List<CompareGroupDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<TransactionRecord>> entry : grouped.entrySet()) {
            List<TransactionRecord> groupRecords = entry.getValue();
            
            // 过滤规则：compareStatus
            int groupSize = groupRecords.size();
            boolean statusMatch = false;
            if ("ALL".equalsIgnoreCase(compareStatus)) {
                statusMatch = true;
            } else if ("UNMATCHED".equalsIgnoreCase(compareStatus)) {
                statusMatch = (groupSize == 1);
            } else {
                // 默认为 MATCHED
                statusMatch = (groupSize > 1);
            }
            
            if (!statusMatch) continue;

            // 过滤规则：channel (如果提供了 channel，组内必须包含该渠道的记录)
            if (channel != null && !channel.isEmpty()) {
                boolean hasChannel = groupRecords.stream().anyMatch(r -> channel.equals(r.getChannel()));
                if (!hasChannel) continue;
            }

            // 过滤规则：accountId (如果提供了 accountId，组内必须包含该账户的记录)
            if (accountId != null) {
                boolean hasAccount = groupRecords.stream().anyMatch(r -> r.getAccount() != null && accountId.equals(r.getAccount().getId()));
                if (!hasAccount) continue;
            }

            CompareGroupDTO dto = new CompareGroupDTO();
            dto.setGroupId(entry.getKey());
            dto.setMainAmount(groupRecords.get(0).getAmount());
            dto.setMainType(groupRecords.get(0).getType());
            dto.setMainDate(groupRecords.get(0).getTransactionTime().toLocalDate().toString());
            
            // 将不同渠道的数据放进去
            Map<String, TransactionRecord> channelData = new HashMap<>();
            for (TransactionRecord tr : groupRecords) {
                channelData.put(tr.getChannel(), tr);
            }
            dto.setChannelRecords(channelData);
            result.add(dto);
        }
        
        // 按照日期倒序
        result.sort((a, b) -> b.getMainDate().compareTo(a.getMainDate()));
        
        return Result.success(result);
    }

    @Data
    public static class CompareGroupDTO {
        private String groupId;
        private BigDecimal mainAmount;
        private String mainType;
        private String mainDate;
        private Map<String, TransactionRecord> channelRecords;
    }
}
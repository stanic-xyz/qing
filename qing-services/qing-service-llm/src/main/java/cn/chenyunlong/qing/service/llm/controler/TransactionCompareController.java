package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/compare")
@Slf4j
public class TransactionCompareController {

    @Autowired
    private TransactionRecordRepository transactionRepo;

    @GetMapping
    public Result<List<CompareGroupDTO>> getCompareData() {
        List<TransactionRecord> allRecords = transactionRepo.findAll()
                .stream().filter(r -> !r.getIsDeleted()).collect(Collectors.toList());

        // 简单的聚合逻辑：按金额 + 类型 + 日期(忽略时分秒) 分组
        Map<String, List<TransactionRecord>> grouped = new HashMap<>();
        
        for (TransactionRecord r : allRecords) {
            if (r.getTransactionTime() == null || r.getAmount() == null) continue;
            String key = r.getAmount().toPlainString() + "_" + r.getType() + "_" + r.getTransactionTime().toLocalDate().toString();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        List<CompareGroupDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<TransactionRecord>> entry : grouped.entrySet()) {
            if (entry.getValue().size() > 1) {
                // 有多条记录，可能来自不同渠道
                CompareGroupDTO dto = new CompareGroupDTO();
                dto.setGroupId(entry.getKey());
                dto.setMainAmount(entry.getValue().get(0).getAmount());
                dto.setMainType(entry.getValue().get(0).getType());
                dto.setMainDate(entry.getValue().get(0).getTransactionTime().toLocalDate().toString());
                
                // 将不同渠道的数据放进去
                Map<String, TransactionRecord> channelData = new HashMap<>();
                for (TransactionRecord tr : entry.getValue()) {
                    channelData.put(tr.getChannel(), tr);
                }
                dto.setChannelRecords(channelData);
                result.add(dto);
            }
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
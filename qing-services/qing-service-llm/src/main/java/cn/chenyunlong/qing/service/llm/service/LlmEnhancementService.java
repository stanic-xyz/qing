package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * LLM 增强服务 - 补充 MatcherService 的能力边界
 * 专门处理 MatcherService 无法判断的语义模糊场景
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmEnhancementService {

    private final LlmClassificationService classificationService;

    /**
     * 对一组记录进行 LLM 增强分类（跳过已有有效分类的记录）
     */
    public void enhanceClassification(List<TransactionRecord> records) {
        if (records == null || records.isEmpty()) return;

        List<TransactionRecord> needEnhancement = records.stream()
            .filter(r -> r.getCategory() == null 
                        && (r.getMerchant() != null || r.getRemark() != null || r.getCounterparty() != null))
            .toList();

        if (needEnhancement.isEmpty()) return;

        log.info("LLM enhancing {} records", needEnhancement.size());
        Map<Long, String> results = classificationService.classifyBatch(needEnhancement);

        for (TransactionRecord record : needEnhancement) {
            String category = results.get(record.getId());
            if (category != null) {
                log.debug("LLM enhanced record {} -> {}", record.getId(), category);
            }
        }
    }

    /**
     * 生成对账结果解释
     */
    public String explainReconciliation(TransactionRecord record) {
        // 简单的规则判断，不需要 LLM
        StringBuilder sb = new StringBuilder();
        sb.append("对账分析: ");
        
        if (record.getReconciliationStatus() != null) {
            sb.append("状态=").append(record.getReconciliationStatus()).append("; ");
        }
        if (record.getMatchStatus() != null) {
            sb.append("匹配状态=").append(record.getMatchStatus()).append("; ");
        }
        if (record.getAmount() != null) {
            sb.append("金额=").append(record.getAmount().toPlainString()).append("; ");
        }
        
        return sb.toString();
    }
}

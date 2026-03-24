package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.MatchRule;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.MatchRuleRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class RuleEngineService {

    @Resource
    private MatchRuleRepository matchRuleRepository;

    public void applyRules(List<TransactionRecord> records) {
        List<MatchRule> rules = matchRuleRepository.findByIsActiveTrueOrderByPriorityDesc();
        if (rules.isEmpty() || records.isEmpty()) {
            return;
        }

        for (TransactionRecord record : records) {
            for (MatchRule rule : rules) {
                try {
                    applyRule(record, rule);
                } catch (Exception e) {
                    log.error("应用规则失败: ruleId={}, error={}", rule.getId(), e.getMessage());
                }
            }
        }
    }

    private void applyRule(TransactionRecord record, MatchRule rule) {
        String ruleType = rule.getRuleType();
        String regex = rule.getMatchRegex();
        String target = rule.getTargetValue();

        if (regex == null || target == null) return;
        Pattern pattern = Pattern.compile(regex);

        if ("COUNTERPARTY_FIX".equals(ruleType)) {
            // 根据 counterparty 或 merchant 匹配
            if (record.getCounterparty() != null && pattern.matcher(record.getCounterparty()).find()) {
                record.setCounterparty(target);
                record.setIsModified(true);
            } else if (record.getMerchant() != null && pattern.matcher(record.getMerchant()).find()) {
                record.setCounterparty(target);
                record.setIsModified(true);
            }
        } else if ("CATEGORY_MAP".equals(ruleType)) {
            // 根据 merchant 匹配分类
            if (record.getMerchant() != null && pattern.matcher(record.getMerchant()).find()) {
                record.setCategory(target);
                record.setIsModified(true);
            }
        }
        // 可扩展其他规则...
    }
}
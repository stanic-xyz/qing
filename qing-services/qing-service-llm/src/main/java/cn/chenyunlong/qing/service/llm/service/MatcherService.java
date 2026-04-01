package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatcherService {

    private final TransactionMatcherRepository matcherRepository;
    private final AccountRepository accountRepository;
    private final CounterpartyRepository counterpartyRepository;

    /**
     * 应用所有启用的匹配器规则对单条记录进行增强
     */
    public void applyMatchers(TransactionRecord record) {
        List<TransactionMatcher> rules = matcherRepository.findByIsActiveTrueOrderByPriorityDesc();
        applyMatchers(record, rules);
    }

    /**
     * 应用指定的匹配器规则列表对单条记录进行增强
     */
    public void applyMatchers(TransactionRecord record, List<TransactionMatcher> rules) {
        List<Account> allAccounts = accountRepository.findAll();

        for (TransactionMatcher rule : rules) {
            // 1. 检查渠道是否匹配
            if (rule.getSourceChannel() != null && !rule.getSourceChannel().isEmpty()) {
                if (!rule.getSourceChannel().equalsIgnoreCase(record.getChannel())) {
                    continue; // 渠道不匹配，跳过
                }
            }

            // 2. 正则匹配 (优先在 merchant 中找，然后 counterparty，最后 remark)
            String targetText = record.getMerchant() + " | " + record.getCounterparty() + " | " + record.getRemark();
            Pattern pattern = Pattern.compile(rule.getMatchRegex());
            Matcher matcher = pattern.matcher(targetText);

            if (matcher.find()) {
                log.debug("Record {} matched rule: {}", record.getOriginalData(), rule.getName());
                boolean modified = false;

                // 如果配置了交易对手 ID，直接应用交易对手的属性
                if (rule.getCounterpartyId() != null) {
                    counterpartyRepository.findById(rule.getCounterpartyId()).ifPresent(cp -> {
                        record.setCounterparty(cp.getName());
                        record.setMerchant(cp.getName());
                        if (cp.getDefaultCategory() != null && !cp.getDefaultCategory().isEmpty()) {
                            record.setCategory(cp.getDefaultCategory());
                        }
                    });
                    modified = true;
                } else {
                    // 提取商户/对手方
                    String newMerchant = extractOrUseFixed(matcher, "merchant", rule.getTargetMerchant());
                    if (newMerchant != null && !newMerchant.isEmpty()) {
                        record.setMerchant(newMerchant);
                        modified = true;
                    }

                    String newCounterparty = extractOrUseFixed(matcher, "counterparty", rule.getTargetCounterparty());
                    if (newCounterparty != null && !newCounterparty.isEmpty()) {
                        record.setCounterparty(newCounterparty);
                        modified = true;
                    }
                }

                // 强制交易类型
                if (rule.getSetType() != null && !rule.getSetType().isEmpty()) {
                    record.setType(rule.getSetType());
                    modified = true;
                }

                // 分类
                if (rule.getTargetCategory() != null && !rule.getTargetCategory().isEmpty()) {
                    record.setCategory(rule.getTargetCategory());
                    modified = true;
                }

                // 内部转账识别
                String targetAccKeyword = extractOrUseFixed(matcher, "account", rule.getTargetAccountKeyword());
                if (targetAccKeyword != null && !targetAccKeyword.isEmpty()) {
                    // 在系统中查找对应的卡号/名称
                    for (Account acc : allAccounts) {
                        if (acc.getCardNumber() != null && targetAccKeyword.contains(acc.getCardNumber()) ||
                            acc.getAccountName() != null && targetAccKeyword.contains(acc.getAccountName())) {

                            record.setTargetAccountId(acc.getId());
                            record.setType("TRANSFER");
                            record.setMatchStatus(MatchStatusEnum.INTERNAL_TRANSFER);
                            record.setMatchRuleName(rule.getName());
                            return; // 转账匹配成功，结束后续规则
                        }
                    }
                }

                // 如果只是一般属性修改，标记为自动匹配
                if (modified) {
                    record.setMatchStatus(MatchStatusEnum.AUTO_MATCHED);
                    record.setMatchRuleName(rule.getName());
                    // 自动匹配不应被标记为人工修改
                    // record.setIsModified(true);
                    return; // 命中高优规则后跳出
                }
            }
        }
    }

    /**
     * 工具方法：尝试从正则捕获组中提取值，如果提取失败则使用固定值
     */
    private String extractOrUseFixed(Matcher matcher, String groupName, String fixedValue) {
        try {
            String extracted = matcher.group(groupName);
            if (extracted != null && !extracted.isEmpty()) {
                return extracted;
            }
        } catch (IllegalArgumentException e) {
            // 没有定义该捕获组
        }
        return fixedValue;
    }
}

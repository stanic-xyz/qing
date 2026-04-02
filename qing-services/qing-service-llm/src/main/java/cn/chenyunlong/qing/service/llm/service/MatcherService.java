package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final TransactionRecordRepository transactionRecordRepository;

    /**
     * 应用所有启用的匹配器规则对单条记录进行增强
     */
    public void applyMatchers(TransactionRecord record) {
        List<TransactionMatcher> rules = matcherRepository.findByIsActiveTrueOrderByPriorityDesc();
        applyMatchers(record, rules);
        // 执行跨账单撮合对账
        applyCrossBillMatch(record);
    }

    /**
     * 跨账单撮合（外部资金对账）
     */
    private void applyCrossBillMatch(TransactionRecord record) {
        if (record.getTransactionTime() == null || record.getAmount() == null) {
            return;
        }

        // 查找前后24小时内金额相同的记录
        LocalDateTime start = record.getTransactionTime().minusHours(24);
        LocalDateTime end = record.getTransactionTime().plusHours(24);
        List<TransactionRecord> candidates = transactionRecordRepository.findByAmountAndTransactionTimeBetweenAndIsImportedTrue(record.getAmount(), start, end);

        for (TransactionRecord candidate : candidates) {
            // 不能和自己匹配，且金额必须完全相等
            if (candidate.getId() != null && candidate.getId().equals(record.getId())) continue;
            if (candidate.getAmount().compareTo(record.getAmount()) != 0) continue;

            // 判断是否一个是 EXTERNAL 钱包流水，一个是银行卡流水
            TransactionRecord walletRecord = null;
            TransactionRecord bankRecord = null;

            if ("EXTERNAL".equalsIgnoreCase(record.getFundType()) && isWallet(record.getChannel()) && !isWallet(candidate.getChannel())) {
                walletRecord = record;
                bankRecord = candidate;
            } else if ("EXTERNAL".equalsIgnoreCase(candidate.getFundType()) && isWallet(candidate.getChannel()) && !isWallet(record.getChannel())) {
                walletRecord = candidate;
                bankRecord = record;
            }

            if (walletRecord != null && bankRecord != null) {
                // 找到匹配！
                // 将银行卡记录改为 TRANSFER
                bankRecord.setType("TRANSFER");
                if (walletRecord.getAccount() != null) {
                    bankRecord.setTargetAccountId(walletRecord.getAccount().getId());
                }
                bankRecord.setMatchStatus(MatchStatusEnum.INTERNAL_TRANSFER);
                bankRecord.setMatchRuleName("跨账单资金撮合");

                // 设置钱包记录的资金来源为银行卡账户
                if (bankRecord.getAccount() != null) {
                    walletRecord.setFundSourceAccountId(bankRecord.getAccount().getId());
                }
                walletRecord.setReconciliationStatus("MATCHED_FUNDING");

                // 由于 candidate 是已在库里的记录，需要 save；而 record 会在后续由 UploadService save
                if (candidate.getId() != null) {
                    transactionRecordRepository.save(candidate);
                }

                log.info("成功撮合跨账单流水：钱包记录={}, 银行记录={}", walletRecord.getOriginalId(), bankRecord.getOriginalId());
                break; // 只撮合一条
            }
        }
    }

    private boolean isWallet(String channel) {
        if (channel == null) return false;
        String upper = channel.toUpperCase();
        return upper.contains("ALIPAY") || upper.contains("WECHAT") || upper.contains("WALLET");
    }

    /**
     * 应用指定的匹配器规则列表对单条记录进行增强
     */
    public void applyMatchers(TransactionRecord record, List<TransactionMatcher> rules) {
        for (TransactionMatcher rule : rules) {
            try {
                if (evaluateCondition(rule.getConditionNode(), record)) {
                    log.debug("Record {} matched rule: {}", record.getOriginalData(), rule.getName());
                    
                    boolean modified = executeActions(rule.getActionNode(), record);
                    
                    if (modified) {
                        record.setMatchStatus(MatchStatusEnum.AUTO_MATCHED);
                        record.setMatchRuleName(rule.getName());
                    }
                    
                    if (Boolean.TRUE.equals(rule.getStopOnMatch())) {
                        log.debug("Rule {} triggered stopOnMatch", rule.getName());
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("执行匹配规则 [{}] 时出错", rule.getName(), e);
            }
        }
    }

    /**
     * 递归计算条件 AST
     */
    private boolean evaluateCondition(JsonNode node, TransactionRecord record) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return true; // 空条件视为全量匹配
        }

        if (node.has("operator") && node.has("children")) {
            String op = node.get("operator").asText().toUpperCase();
            JsonNode children = node.get("children");
            if (children == null || !children.isArray() || children.isEmpty()) {
                return true;
            }
            if ("AND".equals(op)) {
                for (JsonNode child : children) {
                    if (!evaluateCondition(child, record)) return false;
                }
                return true;
            } else if ("OR".equals(op)) {
                for (JsonNode child : children) {
                    if (evaluateCondition(child, record)) return true;
                }
                return false;
            }
        } else if (node.has("field") && node.has("operator")) {
            String field = node.get("field").asText();
            String op = node.get("operator").asText().toUpperCase();
            JsonNode valueNode = node.get("value");
            
            BeanWrapper wrapper = new BeanWrapperImpl(record);
            Object actualValue = null;
            if (wrapper.isReadableProperty(field)) {
                actualValue = wrapper.getPropertyValue(field);
            }
            
            return evaluateLeafCondition(actualValue, op, valueNode);
        }

        return false; // 未知节点结构
    }

    private boolean evaluateLeafCondition(Object actual, String op, JsonNode expectedNode) {
        if (actual == null) {
            return "IS_NULL".equals(op);
        }
        if ("IS_NOT_NULL".equals(op)) {
            return true;
        }

        String actualStr = actual.toString();
        String expectedStr = expectedNode != null ? expectedNode.asText() : "";

        switch (op) {
            case "EQ":
                if (actual instanceof BigDecimal && expectedNode != null && expectedNode.isNumber()) {
                    return ((BigDecimal) actual).compareTo(new BigDecimal(expectedNode.asText())) == 0;
                }
                return actualStr.equals(expectedStr);
            case "NEQ":
                if (actual instanceof BigDecimal && expectedNode != null && expectedNode.isNumber()) {
                    return ((BigDecimal) actual).compareTo(new BigDecimal(expectedNode.asText())) != 0;
                }
                return !actualStr.equals(expectedStr);
            case "CONTAINS":
                return actualStr.contains(expectedStr);
            case "NOT_CONTAINS":
                return !actualStr.contains(expectedStr);
            case "REGEX":
                try {
                    return Pattern.compile(expectedStr).matcher(actualStr).find();
                } catch (Exception e) {
                    return false;
                }
            case "GT":
            case "LT":
            case "GTE":
            case "LTE":
                if (actual instanceof BigDecimal && expectedNode != null && expectedNode.isNumber()) {
                    BigDecimal actualNum = (BigDecimal) actual;
                    BigDecimal expectedNum = new BigDecimal(expectedNode.asText());
                    int cmp = actualNum.compareTo(expectedNum);
                    if ("GT".equals(op)) return cmp > 0;
                    if ("LT".equals(op)) return cmp < 0;
                    if ("GTE".equals(op)) return cmp >= 0;
                    if ("LTE".equals(op)) return cmp <= 0;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * 执行动作节点列表
     */
    private boolean executeActions(JsonNode actionNode, TransactionRecord record) {
        if (actionNode == null || !actionNode.isArray() || actionNode.isEmpty()) {
            return false;
        }

        boolean modified = false;
        for (JsonNode action : actionNode) {
            if (!action.has("actionType")) continue;
            String type = action.get("actionType").asText().toUpperCase();
            JsonNode valueNode = action.get("value");

            switch (type) {
                case "SET_TYPE":
                    if (valueNode != null) {
                        record.setType(valueNode.asText());
                        modified = true;
                    }
                    break;
                case "SET_CATEGORY":
                    if (valueNode != null) {
                        record.setCategory(valueNode.asText());
                        modified = true;
                    }
                    break;
                case "SET_COUNTERPARTY":
                    if (valueNode != null && valueNode.isNumber()) {
                        counterpartyRepository.findById(valueNode.asLong()).ifPresent(cp -> {
                            record.setCounterparty(cp.getName());
                            record.setMerchant(cp.getName());
                            if (cp.getDefaultCategory() != null && !cp.getDefaultCategory().isEmpty()) {
                                record.setCategory(cp.getDefaultCategory());
                            }
                        });
                        modified = true;
                    } else if (valueNode != null) {
                        record.setCounterparty(valueNode.asText());
                        modified = true;
                    }
                    break;
                case "SET_TARGET_ACCOUNT":
                    if (valueNode != null && valueNode.isNumber()) {
                        record.setTargetAccountId(valueNode.asLong());
                        record.setType("TRANSFER");
                        record.setMatchStatus(MatchStatusEnum.INTERNAL_TRANSFER);
                        modified = true;
                    }
                    break;
                case "SET_FUND_SOURCE_ACCOUNT":
                    if (valueNode != null && valueNode.isNumber()) {
                        record.setFundSourceAccountId(valueNode.asLong());
                        modified = true;
                    }
                    break;
            }
        }
        return modified;
    }
}
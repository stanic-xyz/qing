package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.*;
import cn.chenyunlong.qing.service.llm.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatcherService {

    private final TransactionMatcherRepository matcherRepository;
    private final AccountRepository accountRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 应用所有启用的匹配器规则对单条记录进行增强
     */
    public void applyMatchers(UnifiedDraftRecord record) {
        List<TransactionMatcher> rules = matcherRepository.findByIsActiveTrueOrderByPriorityDesc();
        applyMatchers(record, rules);
        // 执行跨账单撮合（精简模式）
        applyCrossBillMatch(record);
    }

    /**
     * 跨账单撮合（精简模式）
     * 当前 UnifiedDraftRecord 模型不包含 fundType/fundSource 等完整字段，
     * 为避免伪造语义，此阶段仅保留方法占位，保证调用链稳定。
     */
    private void applyCrossBillMatch(UnifiedDraftRecord record) {
        if (record == null || record.getTransactionTime() == null || record.getAmount() == null) {
            return;
        }
        log.debug("跳过跨账单撮合（精简模式），recordId={}", record.getId());
    }

    /**
     * 应用指定的匹配器规则列表对单条记录进行增强
     */
    public void applyMatchers(UnifiedDraftRecord record, List<TransactionMatcher> rules) {
        for (TransactionMatcher rule : rules) {
            try {
                if (evaluateCondition(rule.getConditionNode(), record)) {
                    log.debug("Record {} matched rule: {}", record.getId(), rule.getName());

                    boolean modified = executeActions(rule.getActionNode(), record);

                    if (modified) {
                        if (record.getMatchStatus() == null) {
                            record.setMatchStatus(DraftMatchStatusEnum.MATCHED);
                        }
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
    private boolean evaluateCondition(JsonNode node, UnifiedDraftRecord record) {
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
                if (actual instanceof BigDecimal actualNum && expectedNode != null && expectedNode.isNumber()) {
                    BigDecimal expectedNum = new BigDecimal(expectedNode.asText());
                    int cmp = actualNum.compareTo(expectedNum);
                    switch (op) {
                        case "GT" -> {
                            return cmp > 0;
                        }
                        case "LT" -> {
                            return cmp < 0;
                        }
                        case "GTE" -> {
                            return cmp >= 0;
                        }
                        case "LTE" -> {
                            return cmp <= 0;
                        }
                    }
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * 执行动作节点列表
     */
    private boolean executeActions(JsonNode actionNode, UnifiedDraftRecord record) {
        if (actionNode == null || !actionNode.isArray() || actionNode.isEmpty()) {
            return false;
        }

        AtomicBoolean modified = new AtomicBoolean(false);
        for (JsonNode action : actionNode) {
            if (!action.has("actionType")) continue;
            String type = action.get("actionType").asText().toUpperCase();
            JsonNode valueNode = action.get("value");

            switch (type) {
                case "SET_TYPE":
                    if (valueNode != null) {
                        try {
                            record.setTrasactionType(TransactionType.valueOf(valueNode.asText().toUpperCase()));
                            modified.set(true);
                        } catch (IllegalArgumentException ex) {
                            log.warn("无效的交易类型: {}", valueNode.asText());
                        }
                    }
                    break;
                case "SET_CATEGORY":
                    if (valueNode != null) {
                        Optional<Category> categoryOptional = categoryRepository.findById(valueNode.asLong());
                        categoryOptional.ifPresent(category -> {
                            record.setCategory(category);
                            modified.set(true);
                        });
                    }
                    break;
                case "SET_COUNTERPARTY":
                    if (valueNode != null && valueNode.isNumber()) {
                        counterpartyRepository.findById(valueNode.asLong()).ifPresent(counterparty -> {
                            record.setCounterparty(counterparty);
                            if (counterparty.getDefaultCategory() != null) {
                                record.setCategory(counterparty.getDefaultCategory());
                            }
                            modified.set(true);
                        });
                    } else if (valueNode != null) {
                        record.setCounterparty(null);
                        modified.set(true);
                    }
                    break;
                case "SET_TARGET_ACCOUNT":
                    if (valueNode != null && valueNode.isNumber()) {
                        accountRepository.findById(valueNode.asLong()).ifPresent(account -> {
                            record.setTargetAccount(account);
                            record.setTrasactionType(TransactionType.TRANSFER);
                            record.setMatchStatus(DraftMatchStatusEnum.INTERNAL_TRANSFER);
                            modified.set(true);
                        });
                    }
                    break;
                // 关联资金源账户
                case "SET_FUND_SOURCE_ACCOUNT":
                    if (valueNode != null && valueNode.isNumber()) {
                        // UnifiedDraftRecord 当前不落资金来源账户字段，保留动作兼容但不写入。
                        modified.set(true);
                    }
                    break;
            }
        }
        return modified.get();
    }
}

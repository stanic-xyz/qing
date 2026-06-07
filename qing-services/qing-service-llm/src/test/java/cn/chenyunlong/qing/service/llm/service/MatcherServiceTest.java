package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatcherServiceTest {

    @Mock
    private TransactionMatcherRepository matcherRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MatcherService matcherService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ==================== EQ Operator Tests ====================

    @Test
    void testEqStringMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("支付宝", "100.00");
        TransactionMatcher rule = buildRule(
                "EQ字符串匹配",
                "{\"field\":\"merchant\", \"operator\":\"EQ\", \"value\":\"支付宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testEqStringNoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("微信支付", "100.00");
        TransactionMatcher rule = buildRule(
                "EQ字符串不匹配",
                "{\"field\":\"merchant\", \"operator\":\"EQ\", \"value\":\"支付宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testEqBigDecimalMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("商户", "99.50");
        TransactionMatcher rule = buildRule(
                "EQ金额匹配",
                "{\"field\":\"amount\", \"operator\":\"EQ\", \"value\":99.50}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testEqBigDecimalNoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("商户", "100.00");
        TransactionMatcher rule = buildRule(
                "EQ金额不匹配",
                "{\"field\":\"amount\", \"operator\":\"EQ\", \"value\":99.50}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testEqBigDecimalIntegerMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("商户", "100");
        TransactionMatcher rule = buildRule(
                "EQ金额整数匹配",
                "{\"field\":\"amount\", \"operator\":\"EQ\", \"value\":100}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    // ==================== NEQ Operator Tests ====================

    @Test
    void testNeqStringMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("微信支付", "100.00");
        TransactionMatcher rule = buildRule(
                "NEQ字符串匹配",
                "{\"field\":\"merchant\", \"operator\":\"NEQ\", \"value\":\"支付宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testNeqStringNoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("支付宝", "100.00");
        TransactionMatcher rule = buildRule(
                "NEQ字符串不匹配",
                "{\"field\":\"merchant\", \"operator\":\"NEQ\", \"value\":\"支付宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testNeqBigDecimalMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("商户", "100.00");
        TransactionMatcher rule = buildRule(
                "NEQ金额匹配",
                "{\"field\":\"amount\", \"operator\":\"NEQ\", \"value\":99.50}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testNeqBigDecimalNoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("商户", "100.00");
        TransactionMatcher rule = buildRule(
                "NEQ金额不匹配",
                "{\"field\":\"amount\", \"operator\":\"NEQ\", \"value\":100}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== CONTAINS Operator Tests ====================

    @Test
    void testContainsStringMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("淘宝购物", "100.00");
        TransactionMatcher rule = buildRule(
                "CONTAINS字符串匹配",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"淘宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testContainsStringNoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("京东商城", "100.00");
        TransactionMatcher rule = buildRule(
                "CONTAINS字符串不匹配",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"淘宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testContainsAtEnd() throws Exception {
        UnifiedDraftRecord record = baseRecord("中国移动", "50.00");
        TransactionMatcher rule = buildRule(
                "CONTAINS结尾匹配",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"移动\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testContainsInMiddle() throws Exception {
        UnifiedDraftRecord record = baseRecord("星巴克咖啡店", "35.00");
        TransactionMatcher rule = buildRule(
                "CONTAINS中间匹配",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"咖啡\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    // ==================== NOT_CONTAINS Operator Tests ====================

    @Test
    void testNotContainsStringMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("京东商城", "100.00");
        TransactionMatcher rule = buildRule(
                "NOT_CONTAINS字符串匹配",
                "{\"field\":\"merchant\", \"operator\":\"NOT_CONTAINS\", \"value\":\"淘宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testNotContainsStringNoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("淘宝店铺", "100.00");
        TransactionMatcher rule = buildRule(
                "NOT_CONTAINS字符串不匹配",
                "{\"field\":\"merchant\", \"operator\":\"NOT_CONTAINS\", \"value\":\"淘宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testNotContainsWholeFieldMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("淘宝", "100.00");
        TransactionMatcher rule = buildRule(
                "NOT_CONTAINS字段完全相等不匹配",
                "{\"field\":\"merchant\", \"operator\":\"NOT_CONTAINS\", \"value\":\"淘宝\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== REGEX Operator Tests ====================

    @Test
    void testRegexOperator_Match() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail("order-2024-001");
        TransactionMatcher rule = buildRule(
                "REGEX匹配",
                "{\"field\":\"detail\", \"operator\":\"REGEX\", \"value\":\"order-\\\\d{4}-\\\\d{3}\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testRegexOperator_NoMatch() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail("invalid-order");
        TransactionMatcher rule = buildRule(
                "REGEX不匹配",
                "{\"field\":\"detail\", \"operator\":\"REGEX\", \"value\":\"order-\\\\d{4}-\\\\d{3}\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testRegexOperator_InvalidPattern() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail("test");
        TransactionMatcher rule = buildRule(
                "REGEX非法正则",
                "{\"field\":\"detail\", \"operator\":\"REGEX\", \"value\":\"[invalid\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== GT Operator Tests ====================

    @Test
    void testGtOperator() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "150.00");
        TransactionMatcher rule = buildRule(
                "GT大于",
                "{\"field\":\"amount\", \"operator\":\"GT\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testGtOperator_EqualFails() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        TransactionMatcher rule = buildRule(
                "GT等于不匹配",
                "{\"field\":\"amount\", \"operator\":\"GT\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== LT Operator Tests ====================

    @Test
    void testLtOperator() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "50.00");
        TransactionMatcher rule = buildRule(
                "LT小于",
                "{\"field\":\"amount\", \"operator\":\"LT\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    // ==================== GTE Operator Tests ====================

    @Test
    void testGteOperator() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        TransactionMatcher rule = buildRule(
                "GTE大于等于",
                "{\"field\":\"amount\", \"operator\":\"GTE\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testGteOperator_GreaterThanSucceeds() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "150.00");
        TransactionMatcher rule = buildRule(
                "GTE大于也匹配",
                "{\"field\":\"amount\", \"operator\":\"GTE\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    // ==================== LTE Operator Tests ====================

    @Test
    void testLteOperator() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        TransactionMatcher rule = buildRule(
                "LTE小于等于",
                "{\"field\":\"amount\", \"operator\":\"LTE\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testLteOperator_LessThanSucceeds() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "50.00");
        TransactionMatcher rule = buildRule(
                "LTE小于也匹配",
                "{\"field\":\"amount\", \"operator\":\"LTE\", \"value\":100.00}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    // ==================== IS_NULL Operator Tests ====================

    @Test
    void testIsNullOperator() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail(null);
        TransactionMatcher rule = buildRule(
                "IS_NULL匹配",
                "{\"field\":\"detail\", \"operator\":\"IS_NULL\", \"value\":null}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testIsNullOperator_NotNullFails() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail("有值");
        TransactionMatcher rule = buildRule(
                "IS_NULL非空不匹配",
                "{\"field\":\"detail\", \"operator\":\"IS_NULL\", \"value\":null}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== IS_NOT_NULL Operator Tests ====================

    @Test
    void testIsNotNullOperator() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail("has value");
        TransactionMatcher rule = buildRule(
                "IS_NOT_NULL匹配",
                "{\"field\":\"detail\", \"operator\":\"IS_NOT_NULL\", \"value\":null}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testIsNotNullOperator_NullFails() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试商户", "100.00");
        record.setDetail(null);
        TransactionMatcher rule = buildRule(
                "IS_NOT_NULL空不匹配",
                "{\"field\":\"detail\", \"operator\":\"IS_NOT_NULL\", \"value\":null}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== Edge Cases ====================

    @Test
    void testEqCaseSensitive() throws Exception {
        UnifiedDraftRecord record = baseRecord("ALIPAY", "100.00");
        TransactionMatcher rule = buildRule(
                "EQ大小写敏感",
                "{\"field\":\"merchant\", \"operator\":\"EQ\", \"value\":\"alipay\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testContainsCaseSensitive() throws Exception {
        UnifiedDraftRecord record = baseRecord("MyAlipay", "100.00");
        TransactionMatcher rule = buildRule(
                "CONTAINS大小写敏感",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"alipay\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testEqWithNullFieldValue() throws Exception {
        UnifiedDraftRecord record = baseRecord("支付宝", "100.00");
        TransactionMatcher rule = buildRule(
                "EQ匹配null字段",
                "{\"field\":\"remark\", \"operator\":\"EQ\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testContainsWithNullFieldValue() throws Exception {
        UnifiedDraftRecord record = baseRecord("支付宝", "100.00");
        TransactionMatcher rule = buildRule(
                "CONTAINS匹配null字段",
                "{\"field\":\"remark\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testNotContainsWithNullFieldValue() throws Exception {
        UnifiedDraftRecord record = baseRecord("支付宝", "100.00");
        TransactionMatcher rule = buildRule(
                "NOT_CONTAINS匹配null字段",
                "{\"field\":\"remark\", \"operator\":\"NOT_CONTAINS\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        // When actual is null, evaluateLeafCondition returns false for NOT_CONTAINS
        // because it returns early for null values (only IS_NULL returns true for null actual)
        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    // ==================== Existing Tests Preserved ====================

    @Test
    void testSetCategoryByEqRule() throws Exception {
        UnifiedDraftRecord record = baseRecord("鲜农果蔬", "100.50");
        TransactionMatcher rule = buildRule(
                "EQ 规则",
                "{\"field\":\"merchant\", \"operator\":\"EQ\", \"value\":\"鲜农果蔬\"}",
                "[{\"actionType\":\"SET_CATEGORY\", \"value\":1}]",
                false
        );

        Category category = new Category();
        category.setName("餐饮美食");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals("餐饮美食", record.getCategory().getName());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testNestedAndOrCondition() throws Exception {
        UnifiedDraftRecord record = baseRecord("淘宝", "500.00");
        TransactionMatcher rule = buildRule(
                "嵌套逻辑",
                "{\"operator\":\"AND\",\"children\":[{\"operator\":\"OR\",\"children\":[{\"field\":\"merchant\",\"operator\":\"CONTAINS\",\"value\":\"淘宝\"},{\"field\":\"amount\",\"operator\":\"GT\",\"value\":1000}]}]}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testSetCounterpartyAndDefaultCategory() throws Exception {
        UnifiedDraftRecord record = baseRecord("未知商户", "88.00");
        TransactionMatcher rule = buildRule(
                "绑定对手方",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"未知\"}",
                "[{\"actionType\":\"SET_COUNTERPARTY\", \"value\": 10}]",
                false
        );

        Category defaultCategory = new Category();
        defaultCategory.setName("购物");
        Counterparty counterparty = new Counterparty();
        counterparty.setId(10L);
        counterparty.setName("已知大商户");
        counterparty.setDefaultCategory(defaultCategory);
        when(counterpartyRepository.findById(10L)).thenReturn(Optional.of(counterparty));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals("已知大商户", record.getCounterparty().getName());
        assertEquals("购物", record.getCategory().getName());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testStopOnMatchTrue() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试冲突", "120.00");
        record.setMerchant("测试冲突");

        TransactionMatcher rule1 = buildRule(
                "Rule1",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]",
                true
        );
        TransactionMatcher rule2 = buildRule(
                "Rule2",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule1, rule2));

        assertEquals(TransactionType.INCOME, record.getTrasactionType());
    }

    @Test
    void testStopOnMatchFalse() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试冲突", "120.00");
        TransactionMatcher rule1 = buildRule(
                "Rule1",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]",
                false
        );
        TransactionMatcher rule2 = buildRule(
                "Rule2",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}",
                "[{\"actionType\":\"SET_CATEGORY\", \"value\":2}]",
                false
        );

        Category category = new Category();
        category.setName("测试分类");
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        matcherService.applyMatchers(record, List.of(rule1, rule2));

        assertEquals(TransactionType.INCOME, record.getTrasactionType());
        assertEquals("测试分类", record.getCategory().getName());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    void testNeqNotMatched() throws Exception {
        UnifiedDraftRecord record = baseRecord("商户", "100.00");
        TransactionMatcher rule = buildRule(
                "NEQ 不匹配",
                "{\"field\":\"amount\", \"operator\":\"NEQ\", \"value\":100}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testRegexInvalidShouldNotThrow() throws Exception {
        UnifiedDraftRecord record = baseRecord("测试", "30.00");
        TransactionMatcher rule = buildRule(
                "非法正则",
                "{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"[invalid(\"}",
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));
        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    void testSetTargetAccountShouldMarkInternalTransfer() throws Exception {
        UnifiedDraftRecord record = baseRecord("转账", "500.00");
        TransactionMatcher rule = buildRule(
                "设置目标账户",
                "{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"转账\"}",
                "[{\"actionType\":\"SET_TARGET_ACCOUNT\", \"value\": 1}]",
                false
        );

        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(1L, record.getTargetAccount().getId());
        assertEquals(TransactionType.TRANSFER, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.INTERNAL_TRANSFER, record.getMatchStatus());
    }

    // ==================== AND/OR Operator Tests ====================

    @Test
    @DisplayName("AND operator - all children true")
    void testAndOperator_AllTrue() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setDetail("test");
        record.setAmount(new BigDecimal("50.00"));
        record.setMerchant("测试商户");

        String conditionJson = """
            {"operator":"AND","children":[
                {"field":"detail","operator":"EQ","value":"test"},
                {"field":"amount","operator":"LT","value":100.00}
            ]}
            """;
        TransactionMatcher rule = buildRule(
                "AND全匹配",
                conditionJson,
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    @DisplayName("AND operator - one child false")
    void testAndOperator_OneFalse() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setDetail("test");
        record.setAmount(new BigDecimal("150.00"));
        record.setMerchant("测试商户");

        String conditionJson = """
            {"operator":"AND","children":[
                {"field":"detail","operator":"EQ","value":"test"},
                {"field":"amount","operator":"LT","value":100.00}
            ]}
            """;
        TransactionMatcher rule = buildRule(
                "AND一个假",
                conditionJson,
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    @DisplayName("OR operator - one child true")
    void testOrOperator_OneTrue() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setDetail("test");
        record.setAmount(new BigDecimal("150.00"));
        record.setMerchant("测试商户");

        String conditionJson = """
            {"operator":"OR","children":[
                {"field":"detail","operator":"EQ","value":"test"},
                {"field":"amount","operator":"LT","value":100.00}
            ]}
            """;
        TransactionMatcher rule = buildRule(
                "OR一个真",
                conditionJson,
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    @Test
    @DisplayName("OR operator - all children false")
    void testOrOperator_AllFalse() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setDetail("other");
        record.setAmount(new BigDecimal("150.00"));
        record.setMerchant("测试商户");

        String conditionJson = """
            {"operator":"OR","children":[
                {"field":"detail","operator":"EQ","value":"test"},
                {"field":"amount","operator":"LT","value":100.00}
            ]}
            """;
        TransactionMatcher rule = buildRule(
                "OR全假",
                conditionJson,
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertNull(record.getTrasactionType());
        assertNull(record.getMatchStatus());
    }

    @Test
    @DisplayName("Nested AND/OR - complex AST with depth 3")
    void testNestedAndOrCondition_Depth3() throws Exception {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setDetail("online-payment");
        record.setAmount(new BigDecimal("200.00"));
        record.setMerchant("测试商户");

        // OR(
        //   AND(CONTAINS(detail, "online"), GT(amount, 100)),
        //   AND(CONTAINS(detail, "payment"), GTE(amount, 50))
        // )
        String conditionJson = """
            {"operator":"OR","children":[
                {"operator":"AND","children":[
                    {"field":"detail","operator":"CONTAINS","value":"online"},
                    {"field":"amount","operator":"GT","value":100.00}
                ]},
                {"operator":"AND","children":[
                    {"field":"detail","operator":"CONTAINS","value":"payment"},
                    {"field":"amount","operator":"GTE","value":50.00}
                ]}
            ]}
            """;
        TransactionMatcher rule = buildRule(
                "嵌套AND/OR深度3",
                conditionJson,
                "[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]",
                false
        );

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TransactionType.EXPENSE, record.getTrasactionType());
        assertEquals(DraftMatchStatusEnum.MATCHED, record.getMatchStatus());
    }

    /**
     * 构造基础草稿记录，减少重复样板代码。
     */
    private UnifiedDraftRecord baseRecord(String merchant, String amount) {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setMerchant(merchant);
        record.setAmount(new BigDecimal(amount));
        return record;
    }

    /**
     * 构建匹配规则实体，统一测试数据定义方式。
     */
    private TransactionMatcher buildRule(String name, String conditionJson, String actionJson, boolean stopOnMatch) throws Exception {
        TransactionMatcher rule = new TransactionMatcher();
        rule.setName(name);
        rule.setConditionNode(objectMapper.readTree(conditionJson));
        rule.setActionNode(objectMapper.readTree(actionJson));
        rule.setStopOnMatch(stopOnMatch);
        return rule;
    }
}

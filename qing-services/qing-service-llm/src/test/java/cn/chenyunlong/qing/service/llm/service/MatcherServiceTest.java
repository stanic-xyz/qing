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

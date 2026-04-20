package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatcherServiceTest {

    @Mock
    private TransactionMatcherRepository matcherRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @Mock
    private TransactionRecordRepository transactionRecordRepository;
    @Mock
    private cn.chenyunlong.qing.service.llm.repository.CategoryRepository categoryRepository;

    @InjectMocks
    private MatcherService matcherService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testEvaluateLeafCondition_EQ() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("鲜农果蔬");
        record.setAmount(new BigDecimal("100.50"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test EQ");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"EQ\", \"value\":\"鲜农果蔬\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_CATEGORY\", \"value\":1}]"));

        Category mockCategory = new Category();
        mockCategory.setName("餐饮美食");
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals("餐饮美食", record.getCategory().getName());
        assertEquals(MatchStatusEnum.AUTO_MATCHED, record.getMatchStatus());
    }

    @Test
    public void testEvaluateCondition_AND_OR_Nested() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("淘宝");
        record.setAmount(new BigDecimal("500.00"));

        String conditionJson = "{" +
                "\"operator\": \"AND\"," +
                "\"children\": [" +
                "  {" +
                "    \"operator\": \"OR\"," +
                "    \"children\": [" +
                "      {\"field\": \"merchant\", \"operator\": \"CONTAINS\", \"value\": \"淘宝\"}," +
                "      {\"field\": \"amount\", \"operator\": \"GT\", \"value\": 1000}" +
                "    ]" +
                "  }" +
                "]" +
                "}";

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Nested Logic");
        rule.setConditionNode(objectMapper.readTree(conditionJson));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));
        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testExecuteActions_SetCounterpartyWithMock() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("未知商户");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Bind Counterparty");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"未知\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_COUNTERPARTY\", \"value\": 1}]"));

        Counterparty mockCp = new Counterparty();
        mockCp.setId(1L);
        mockCp.setName("已知大商户");

        Category defaultCategory = new Category();
        defaultCategory.setName("购物");

        mockCp.setDefaultCategory(defaultCategory);

        when(counterpartyRepository.findById(1L)).thenReturn(Optional.of(mockCp));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals("已知大商户", record.getCounterparty().getName());
        assertEquals("未知商户", record.getMerchant());
        assertEquals("购物", record.getCategory().getName());
    }

    @Test
    public void testStopOnMatch_True() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setRemark("测试冲突");

        TransactionMatcher rule1 = new TransactionMatcher();
        rule1.setName("Rule1");
        rule1.setStopOnMatch(true); // 阻断
        rule1.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        rule1.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]"));

        TransactionMatcher rule2 = new TransactionMatcher();
        rule2.setName("Rule2");
        rule2.setStopOnMatch(false);
        rule2.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        rule2.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        // 假设按照优先级，rule1先执行
        matcherService.applyMatchers(record, Arrays.asList(rule1, rule2));

        // rule1 命中且阻断，所以不会执行 rule2
        assertEquals(TrasactionType.INCOME, record.getType());
        assertEquals("Rule1", record.getMatchRuleName());
    }

    @Test
    public void testStopOnMatch_False() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setRemark("测试冲突");

        TransactionMatcher rule1 = new TransactionMatcher();
        rule1.setName("Rule1");
        rule1.setStopOnMatch(false); // 不阻断
        rule1.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        rule1.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]"));

        TransactionMatcher rule2 = new TransactionMatcher();
        rule2.setName("Rule2");
        rule2.setStopOnMatch(false);
        rule2.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        rule2.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_CATEGORY\", \"value\":2}]"));

        Category mockCategory = new Category();
        mockCategory.setName("测试分类");
        lenient().when(categoryRepository.findById(2L)).thenReturn(Optional.of(mockCategory));

        // 假设按照优先级，rule1先执行
        matcherService.applyMatchers(record, Arrays.asList(rule1, rule2));

        // rule1 命中但不阻断，所以 rule2 也会执行
        assertEquals(TrasactionType.INCOME, record.getType());
        assertEquals("测试分类", record.getCategory().getName());
        // ruleName 被最后一次命中的规则覆盖
        assertEquals("Rule2", record.getMatchRuleName());
    }

    // ==================== 6.1 操作符测试 ====================

    @Test
    public void testEvaluateLeafCondition_NEQ() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("150.00")); // 150 != 100, NEQ should match

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test NEQ");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"NEQ\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals(MatchStatusEnum.AUTO_MATCHED, record.getMatchStatus());
    }

    @Test
    public void testEvaluateLeafCondition_NEQ_NotMatch() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00")); // 100 == 100, NEQ should NOT match

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test NEQ Not Match");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"NEQ\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 条件不匹配（金额正好等于100），所以不执行动作
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
        assertNull(record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_GT() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("150.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test GT");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"GT\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals(MatchStatusEnum.AUTO_MATCHED, record.getMatchStatus());
    }

    @Test
    public void testEvaluateLeafCondition_LT() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("50.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test LT");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"LT\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_GTE() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test GTE");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"GTE\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_LTE() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test LTE");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"LTE\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_REGEX_Simple() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("淘宝订单12345");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test REGEX Simple");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"淘宝.*\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals(MatchStatusEnum.AUTO_MATCHED, record.getMatchStatus());
    }

    @Test
    public void testEvaluateLeafCondition_REGEX_Complex() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("淘宝天猫超市订单");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test REGEX Complex");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"淘宝.*(天猫|超市)\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_REGEX_NoMatch() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("京东订单");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test REGEX No Match");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"淘宝.*\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 不匹配，不执行动作
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testEvaluateLeafCondition_REGEX_Invalid() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("测试");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test REGEX Invalid");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"[invalid(\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        // 无效正则不应抛出异常，只是匹配失败
        matcherService.applyMatchers(record, List.of(rule));
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testEvaluateLeafCondition_IS_NULL() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setRemark(null);

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test IS_NULL");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"IS_NULL\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_IS_NOT_NULL() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setRemark("有备注的交易");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test IS_NOT_NULL");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"IS_NOT_NULL\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_NOT_CONTAINS() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("京东商城");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test NOT_CONTAINS");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"NOT_CONTAINS\", \"value\":\"淘宝\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testEvaluateLeafCondition_NOT_CONTAINS_NoMatch() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("淘宝商城");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Test NOT_CONTAINS No Match");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"NOT_CONTAINS\", \"value\":\"淘宝\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 包含"淘宝"，所以 NOT_CONTAINS 不匹配
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    // ==================== 6.2 多动作链测试 ====================

    @Test
    public void testMultipleActionsChain() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("TestMerchant");
        record.setAmount(new BigDecimal("200.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Multiple Actions Rule");
        // 只用SET_TYPE和SET_COUNTERPARTY，因为SET_COUNTERPARTY会覆盖SET_CATEGORY
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"Test\"}"));
        rule.setActionNode(objectMapper.readTree("[" +
                "{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}," +
                "{\"actionType\":\"SET_COUNTERPARTY\", \"value\":2}" +
                "]"));

        Counterparty mockCp = new Counterparty();
        mockCp.setId(2L);
        mockCp.setName("TestMerchant");
        Category defaultCategory = new Category();
        defaultCategory.setName("Shopping");
        mockCp.setDefaultCategory(defaultCategory);
        lenient().when(counterpartyRepository.findById(2L)).thenReturn(Optional.of(mockCp));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals("TestMerchant", record.getCounterparty().getName());
        // SET_COUNTERPARTY会将category设置为counterparty的defaultCategory
        assertEquals("Shopping", record.getCategory().getName());
        assertEquals(MatchStatusEnum.AUTO_MATCHED, record.getMatchStatus());
    }

    // ==================== 6.3 优先级测试 ====================

    @Test
    public void testPriorityOrderExecution() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("测试");
        record.setAmount(new BigDecimal("100.00"));

        // 低优先级规则
        TransactionMatcher lowPriorityRule = new TransactionMatcher();
        lowPriorityRule.setName("低优先级规则");
        lowPriorityRule.setPriority(10);
        lowPriorityRule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        lowPriorityRule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]"));

        // 高优先级规则（stopOnMatch=false，让规则链继续执行）
        TransactionMatcher highPriorityRule = new TransactionMatcher();
        highPriorityRule.setName("高优先级规则");
        highPriorityRule.setPriority(100);
        highPriorityRule.setStopOnMatch(false);
        highPriorityRule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"GT\", \"value\":50}"));
        highPriorityRule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        // 按优先级排序后应用
        List<TransactionMatcher> sortedRules = Arrays.asList(highPriorityRule, lowPriorityRule);
        matcherService.applyMatchers(record, sortedRules);

        // highPriority先执行（amount GT 50 = true），设置type=EXPENSE
        // 然后lowPriority执行（merchant CONTAINS "测试" = true），覆盖type=INCOME
        assertEquals(TrasactionType.INCOME, record.getType());
        // matchRuleName是最后执行的规则名
        assertEquals("低优先级规则", record.getMatchRuleName());
    }

    @Test
    public void testHighPriorityRuleStopsLowPriority() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("测试");
        record.setAmount(new BigDecimal("100.00"));

        // 高优先级规则，带stopOnMatch
        TransactionMatcher highPriorityRule = new TransactionMatcher();
        highPriorityRule.setName("高优先级规则");
        highPriorityRule.setPriority(100);
        highPriorityRule.setStopOnMatch(true);
        highPriorityRule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        highPriorityRule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        // 低优先级规则
        TransactionMatcher lowPriorityRule = new TransactionMatcher();
        lowPriorityRule.setName("低优先级规则");
        lowPriorityRule.setPriority(10);
        lowPriorityRule.setStopOnMatch(false);
        lowPriorityRule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        lowPriorityRule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]"));

        // 按优先级排序后应用
        List<TransactionMatcher> sortedRules = Arrays.asList(highPriorityRule, lowPriorityRule);
        matcherService.applyMatchers(record, sortedRules);

        // 高优先级规则命中后停止，低优先级规则不会执行
        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals("高优先级规则", record.getMatchRuleName());
    }

    // ==================== 6.4 嵌套条件深度测试 ====================

    @Test
    public void testDeeplyNestedConditions_ThreeLevels() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("淘宝");
        record.setAmount(new BigDecimal("500.00"));
        record.setRemark("网购");

        // 3层嵌套: AND(OR(AND(...)), ...)
        String conditionJson = "{" +
                "\"operator\": \"AND\"," +
                "\"children\": [" +
                "  {" +
                "    \"operator\": \"OR\"," +
                "    \"children\": [" +
                "      {\"field\": \"merchant\", \"operator\": \"CONTAINS\", \"value\": \"淘宝\"}," +
                "      {\"field\": \"remark\", \"operator\": \"CONTAINS\", \"value\": \"京东\"}" +
                "    ]" +
                "  }," +
                "  {" +
                "    \"operator\": \"AND\"," +
                "    \"children\": [" +
                "      {\"field\": \"amount\", \"operator\": \"GTE\", \"value\": 100}," +
                "      {\"field\": \"amount\", \"operator\": \"LTE\", \"value\": 1000}" +
                "    ]" +
                "  }" +
                "]" +
                "}";

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Deeply Nested");
        rule.setConditionNode(objectMapper.readTree(conditionJson));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 淘宝 AND (金额>=100 AND <=1000) => 匹配
        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testMixedFieldsInNestedConditions() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("沃尔玛");
        record.setAmount(new BigDecimal("300.00"));
        record.setRemark("超市购物");

        // 混合使用merchant, amount, remark字段
        String conditionJson = "{" +
                "\"operator\": \"AND\"," +
                "\"children\": [" +
                "  {\"field\": \"merchant\", \"operator\": \"REGEX\", \"value\": \".*沃尔玛.*\"}," +
                "  {\"field\": \"amount\", \"operator\": \"GT\", \"value\": 100}," +
                "  {\"field\": \"remark\", \"operator\": \"CONTAINS\", \"value\": \"购物\"}" +
                "]" +
                "}";

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Mixed Fields");
        rule.setConditionNode(objectMapper.readTree(conditionJson));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"},{\"actionType\":\"SET_CATEGORY\", \"value\":1}]"));

        Category mockCategory = new Category();
        mockCategory.setName("购物");
        lenient().when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals("购物", record.getCategory().getName());
    }

    // ==================== 6.5 空值处理测试 ====================

    @Test
    public void testNullField_NotNullOperator() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant(null);

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Not Null Rule");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"IS_NOT_NULL\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // merchant为null，IS_NOT_NULL不匹配
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testNullField_ContainsOperator() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant(null);

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Contains Rule");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测试\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // null转换为空字符串，不包含"测试"
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testEmptyString_vs_Null() throws Exception {
        TransactionRecord recordWithEmpty = new TransactionRecord();
        recordWithEmpty.setMerchant("");
        recordWithEmpty.setRemark("有备注");

        TransactionMatcher emptyRule = new TransactionMatcher();
        emptyRule.setName("Empty String Rule");
        emptyRule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"CONTAINS\", \"value\":\"测\"}"));
        emptyRule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(recordWithEmpty, List.of(emptyRule));

        // 空字符串不包含"测"
        assertEquals(MatchStatusEnum.ORIGINAL, recordWithEmpty.getMatchStatus());
    }

    // ==================== 6.6 金额边界值测试 ====================

    @Test
    public void testAmountBoundary_GT_ExactlyEqual() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("GT Boundary");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"GT\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 正好等于100，GT不匹配
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testAmountBoundary_LT_ExactlyEqual() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("LT Boundary");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"LT\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 正好等于100，LT不匹配
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testAmountBoundary_GTE_ExactlyEqual() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("GTE Boundary");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"GTE\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 正好等于100，GTE匹配
        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testAmountBoundary_LTE_ExactlyEqual() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.00"));

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("LTE Boundary");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"LTE\", \"value\":100}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        // 正好等于100，LTE匹配
        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testAmountBoundary_DecimalPrecision() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setAmount(new BigDecimal("100.01"));

        TransactionMatcher ruleGT = new TransactionMatcher();
        ruleGT.setName("GT 100.00");
        ruleGT.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"GT\", \"value\":100}"));
        ruleGT.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(ruleGT));

        // 100.01 > 100，GT匹配
        assertEquals(TrasactionType.EXPENSE, record.getType());
        assertEquals(MatchStatusEnum.AUTO_MATCHED, record.getMatchStatus());

        // 重置状态用于第二个测试
        record.setMatchStatus(MatchStatusEnum.ORIGINAL);
        record.setType(null);

        TransactionMatcher ruleLT = new TransactionMatcher();
        ruleLT.setName("LT 100.01");
        ruleLT.setConditionNode(objectMapper.readTree("{\"field\":\"amount\", \"operator\":\"LT\", \"value\":100.01}"));
        ruleLT.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"INCOME\"}]"));

        matcherService.applyMatchers(record, List.of(ruleLT));

        // 100.01 < 100.01 不成立，所以type仍然是null，matchStatus仍然是ORIGINAL
        assertNull(record.getType());
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    // ==================== 6.7 正则表达式测试 ====================

    @Test
    public void testRegexPatterns_DigitOnly() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("订单号123456");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Digit Pattern");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"\\\\d+\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }

    @Test
    public void testRegexPatterns_NegativeCase() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setMerchant("没有数字的商户");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Digit Pattern Negative");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\", \"operator\":\"REGEX\", \"value\":\"\\\\d+\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
    }

    @Test
    public void testRegexPatterns_EmailLike() throws Exception {
        TransactionRecord record = new TransactionRecord();
        record.setRemark("支付给 test@example.com");

        TransactionMatcher rule = new TransactionMatcher();
        rule.setName("Email Pattern");
        rule.setConditionNode(objectMapper.readTree("{\"field\":\"remark\", \"operator\":\"REGEX\", \"value\":\"[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}\"}"));
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\", \"value\":\"EXPENSE\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals(TrasactionType.EXPENSE, record.getType());
    }
}

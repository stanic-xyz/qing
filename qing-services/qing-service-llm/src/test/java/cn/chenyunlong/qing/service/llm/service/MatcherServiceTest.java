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
        rule.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_CATEGORY\", \"value\":\"餐饮美食\"}]"));

        matcherService.applyMatchers(record, List.of(rule));

        assertEquals("餐饮美食", record.getCategory());
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
        assertEquals("INCOME", record.getType());
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
        rule2.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_CATEGORY\", \"value\":\"测试分类\"}]"));

        // 假设按照优先级，rule1先执行
        matcherService.applyMatchers(record, Arrays.asList(rule1, rule2));

        // rule1 命中但不阻断，所以 rule2 也会执行
        assertEquals("INCOME", record.getType());
        assertEquals("测试分类", record.getCategory());
        // ruleName 被最后一次命中的规则覆盖
        assertEquals("Rule2", record.getMatchRuleName());
    }
}

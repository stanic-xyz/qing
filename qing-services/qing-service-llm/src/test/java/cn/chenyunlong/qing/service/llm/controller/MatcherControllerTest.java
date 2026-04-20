package cn.chenyunlong.qing.service.llm.controller;

import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MatcherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionMatcherRepository matcherRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionMatcher testMatcher;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        matcherRepository.deleteAll();
        transactionRecordRepository.deleteAll();

        // 创建测试规则
        testMatcher = new TransactionMatcher();
        testMatcher.setName("测试规则");
        testMatcher.setDescription("测试规则描述");
        testMatcher.setRuleType("MERCHANT");
        testMatcher.setPriority(100);
        testMatcher.setStopOnMatch(true);
        testMatcher.setIsActive(true);
        testMatcher.setConditionNode(objectMapper.readTree("{\"operator\":\"AND\",\"children\":[]}"));
        testMatcher.setActionNode(objectMapper.readTree("[]"));
        testMatcher = matcherRepository.save(testMatcher);
    }

    @Test
    public void testGetAllMatchers() throws Exception {
        mockMvc.perform(get("/api/finance/matchers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("测试规则"));
    }

    @Test
    public void testGetActiveMatchers() throws Exception {
        mockMvc.perform(get("/api/finance/matchers/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("测试规则"))
                .andExpect(jsonPath("$.data[0].isActive").value(true));
    }

    @Test
    public void testSaveNewMatcher() throws Exception {
        TransactionMatcher newMatcher = new TransactionMatcher();
        newMatcher.setName("新建规则");
        newMatcher.setRuleType("CHANNEL");
        newMatcher.setPriority(50);
        newMatcher.setStopOnMatch(false);
        newMatcher.setIsActive(true);
        newMatcher.setConditionNode(objectMapper.readTree("{\"field\":\"merchant\",\"operator\":\"CONTAINS\",\"value\":\"测试\"}"));
        newMatcher.setActionNode(objectMapper.readTree("[{\"actionType\":\"SET_TYPE\",\"value\":\"EXPENSE\"}]"));

        mockMvc.perform(post("/api/finance/matchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMatcher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("新建规则"))
                .andExpect(jsonPath("$.data.ruleType").value("CHANNEL"));
    }

    @Test
    public void testUpdateMatcher() throws Exception {
        testMatcher.setName("更新后的规则名");
        testMatcher.setPriority(200);

        mockMvc.perform(put("/api/finance/matchers/" + testMatcher.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMatcher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("更新后的规则名"))
                .andExpect(jsonPath("$.data.priority").value(200));
    }

    @Test
    public void testDeleteMatcher() throws Exception {
        mockMvc.perform(delete("/api/finance/matchers/" + testMatcher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证已删除
        mockMvc.perform(get("/api/finance/matchers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void testToggleMatcherStatus() throws Exception {
        // 先停用
        testMatcher.setIsActive(false);
        mockMvc.perform(post("/api/finance/matchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMatcher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isActive").value(false));

        // 再启用
        testMatcher.setIsActive(true);
        mockMvc.perform(post("/api/finance/matchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMatcher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isActive").value(true));
    }

    @Test
    public void testGetMatchersSortedByPriority() throws Exception {
        // 创建多个不同优先级的规则
        TransactionMatcher lowPriority = new TransactionMatcher();
        lowPriority.setName("低优先级规则");
        lowPriority.setRuleType("COUNTERPARTY");
        lowPriority.setPriority(10);
        lowPriority.setStopOnMatch(true);
        lowPriority.setIsActive(true);
        lowPriority.setConditionNode(objectMapper.readTree("{\"field\":\"amount\",\"operator\":\"GT\",\"value\":0}"));
        lowPriority.setActionNode(objectMapper.readTree("[]"));
        matcherRepository.save(lowPriority);

        TransactionMatcher highPriority = new TransactionMatcher();
        highPriority.setName("高优先级规则");
        highPriority.setRuleType("CHANNEL");
        highPriority.setPriority(500);
        highPriority.setStopOnMatch(true);
        highPriority.setIsActive(true);
        highPriority.setConditionNode(objectMapper.readTree("{\"field\":\"amount\",\"operator\":\"GT\",\"value\":0}"));
        highPriority.setActionNode(objectMapper.readTree("[]"));
        matcherRepository.save(highPriority);

        mockMvc.perform(get("/api/finance/matchers/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].name").value("高优先级规则"))
                .andExpect(jsonPath("$.data[0].priority").value(500));
    }
}

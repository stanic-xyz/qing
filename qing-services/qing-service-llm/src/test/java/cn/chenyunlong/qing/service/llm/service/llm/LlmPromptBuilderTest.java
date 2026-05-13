package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prompt 构建器测试
 */
class LlmPromptBuilderTest {

    private LlmPromptBuilder promptBuilder;

    @BeforeEach
    void setUp() {
        promptBuilder = new LlmPromptBuilder();
    }

    @Test
    void testBuildPrompt() {
        String rawText = "2024-01-01 京东购物 123.45元";
        LlmParseContextLoader.SystemContext context = new LlmParseContextLoader.SystemContext(
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList()
        );

        String prompt = promptBuilder.buildPrompt(rawText, context, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(prompt);
        assertTrue(prompt.contains("【原始账单文本】"));
        assertTrue(prompt.contains("【系统上下文】"));
        assertTrue(prompt.contains("【分类策略】"));
        assertTrue(prompt.contains("2024-01-01 京东购物 123.45元"));
    }

    @Test
    void testBuildPromptByPlatform() {
        String rawText = "2024-01-01 京东购物 123.45元";
        LlmParseContextLoader.SystemContext context = new LlmParseContextLoader.SystemContext(
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList()
        );

        String prompt = promptBuilder.buildPrompt(rawText, context, CategoryStrategy.BY_PLATFORM);

        assertNotNull(prompt);
        assertTrue(prompt.contains("BY_PLATFORM"));
        assertTrue(prompt.contains("京东"));
    }

    @Test
    void testBuildPromptHybrid() {
        String rawText = "2024-01-01 京东购物 123.45元";
        LlmParseContextLoader.SystemContext context = new LlmParseContextLoader.SystemContext(
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList(),
                java.util.Collections.emptyList()
        );

        String prompt = promptBuilder.buildPrompt(rawText, context, CategoryStrategy.HYBRID);

        assertNotNull(prompt);
        assertTrue(prompt.contains("HYBRID"));
        assertTrue(prompt.contains("混合"));
    }

    @Test
    void testBuildTestPrompt() {
        String prompt = promptBuilder.buildTestPrompt();

        assertNotNull(prompt);
        assertTrue(prompt.contains("测试任务"));
    }
}

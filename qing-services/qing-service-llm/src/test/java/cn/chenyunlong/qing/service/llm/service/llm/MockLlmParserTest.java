package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Mock LLM 解析器测试
 */
class MockLlmParserTest {

    private MockLlmParser mockParser;

    @BeforeEach
    void setUp() {
        mockParser = new MockLlmParser();
    }

    @Test
    void testParseWithJingdong() throws Exception {
        String prompt = """
                【原始账单文本】
                2024-01-01 京东 购买商品 123.45元
                2024-01-02 京东 购买数码 5999.00元
                【系统上下文】
                分类列表：
                - 餐饮美食 (ID:1)
                - 数码电子 (ID:3)
                - 娱乐 (ID:5)
                """;

        String result = mockParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        assertTrue(result.contains("success"));
        assertTrue(result.contains("京东"));
        assertTrue(result.contains("数码电子"));
    }

    @Test
    void testParseWithMeituan() throws Exception {
        String prompt = """
                【原始账单文本】
                2024-01-01 美团外卖 餐饮 45.00元
                2024-01-02 饿了么订餐 30.00元
                【系统上下文】
                分类列表：
                - 餐饮美食 (ID:1)
                - 数码电子 (ID:3)
                """;

        String result = mockParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        assertTrue(result.contains("美团"));
        assertTrue(result.contains("餐饮美食"));
    }

    @Test
    void testParseWithDouyin() throws Exception {
        String prompt = """
                【原始账单文本】
                2024-01-01 抖音直播充值 50.00元
                2024-01-02 抖音打赏 100.00元
                【系统上下文】
                分类列表：
                - 娱乐 (ID:5)
                """;

        String result = mockParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        assertTrue(result.contains("抖音"));
        assertTrue(result.contains("娱乐"));
        // 应该建议新建分类
        assertTrue(result.contains("直播娱乐") || result.contains("suggestedNewCategories"));
    }

    @Test
    void testParseWithByPlatformStrategy() throws Exception {
        String prompt = """
                【原始账单文本】
                2024-01-01 京东购物 123.45元
                2024-01-02 美团外卖 45.00元
                【系统上下文】
                """;

        String result = mockParser.parse(prompt, CategoryStrategy.BY_PLATFORM);

        assertNotNull(result);
        assertTrue(result.contains("BY_PLATFORM"));
    }

    @Test
    void testParseReturnsJson() throws Exception {
        String prompt = """
                【原始账单文本】
                2024-01-01 测试交易 100.00元
                【系统上下文】
                """;

        String result = mockParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        // 应该是有效的 JSON
        assertTrue(result.startsWith("{") || result.startsWith("{\""));
        assertTrue(result.contains("records"));
        assertTrue(result.contains("summary"));
    }

    @Test
    void testParseWithEmptyText() throws Exception {
        String prompt = """
                【原始账单文本】

                【系统上下文】
                """;

        String result = mockParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        assertTrue(result.contains("success"));
    }
}

package cn.chenyunlong.qing.service.llm.service.llm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件内容提取器测试
 */
class LlmFileContentExtractorTest {

    private LlmFileContentExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new LlmFileContentExtractor();
    }

    @Test
    void testExtractTextFromCsv() {
        String csvContent = """
                日期,描述,金额
                2024-01-01,京东购物,123.45
                2024-01-02,美团外卖,45.00
                2024-01-03,抖音充值,50.00
                """;

        String result = extractor.extractTextFromCsv(csvContent.getBytes());
        assertNotNull(result);
        assertTrue(result.contains("2024-01-01"));
        assertTrue(result.contains("京东购物"));
        assertTrue(result.contains("123.45"));
    }

    @Test
    void testExtractTextFromText() {
        String textContent = """
                2024-01-01 京东 123.45元
                2024-01-02 美团 45.00元
                2024-01-03 抖音 50.00元
                """;

        String result = extractor.extractTextFromText(textContent.getBytes());
        assertNotNull(result);
        assertTrue(result.contains("京东"));
        assertTrue(result.contains("123.45"));
    }

    @Test
    void testExtractByFileExtension() {
        String textContent = "2024-01-01 测试内容 123.45元";

        // Test CSV
        String csvResult = extractor.extract(textContent.getBytes(), "test.csv");
        assertTrue(csvResult.contains("测试内容"));

        // Test TXT
        String txtResult = extractor.extract(textContent.getBytes(), "test.txt");
        assertTrue(txtResult.contains("测试内容"));
    }

    @Test
    void testParseAmounts() {
        String text = "京东购物 123.45元，美团外卖 45.00元，抖音充值 50元";

        List<BigDecimal> amounts = extractor.parseAmounts(text);

        assertNotNull(amounts);
        assertTrue(amounts.size() >= 3);
        assertTrue(amounts.stream().anyMatch(a -> a.compareTo(new BigDecimal("123.45")) == 0));
    }

    @Test
    void testParseAmountsInvalid() {
        String text = "这是一个没有金额的文本";

        List<BigDecimal> amounts = extractor.parseAmounts(text);

        assertNotNull(amounts);
        assertTrue(amounts.isEmpty());
    }
}

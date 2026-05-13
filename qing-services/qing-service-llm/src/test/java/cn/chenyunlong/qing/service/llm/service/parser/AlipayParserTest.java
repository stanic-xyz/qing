package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Requires local sample bills")
class AlipayParserTest extends BaseParserTest {

    /**
     * 解析器基础冒烟测试：仅验证可解析且关键字段非空。
     */
    @Test
    void testParseSmoke() throws Exception {
        if (!resourceExists("alipay/alipay_test.csv")) {
            return;
        }

        try (InputStream is = getResourceAsStream("alipay/alipay_test.csv")) {
            AlipayParser parser = new AlipayParser();
            ParseResult result = parser.parse(is, "alipay_test.csv");
            List<UnifiedDraftRecord> records = result.getRecords();

            assertFalse(records.isEmpty());
            UnifiedDraftRecord first = records.get(0);
            assertNotNull(first.getTransactionTime());
            assertNotNull(first.getAmount());
        }
    }
}
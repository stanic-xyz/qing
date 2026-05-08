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
class CmbParserTest extends BaseParserTest {

    /**
     * 解析器基础冒烟测试：仅验证可解析且关键字段非空。
     */
    @Test
    void testParseSmoke() throws Exception {
        if (!resourceExists("cmb/xlsx/cmb_test.xlsx")) {
            return;
        }

        try (InputStream is = getResourceAsStream("cmb/xlsx/cmb_test.xlsx")) {
            CmbParser parser = new CmbParser();
            ParseResult result = parser.parse(is, "cmb_test.xlsx");
            List<UnifiedDraftRecord> records = result.getRecords();

            assertFalse(records.isEmpty());
            UnifiedDraftRecord first = records.get(0);
            assertNotNull(first.getTransactionTime());
            assertNotNull(first.getAmount());
        }
    }
}
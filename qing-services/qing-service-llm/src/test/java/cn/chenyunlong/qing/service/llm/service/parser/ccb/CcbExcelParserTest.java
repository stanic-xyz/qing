package cn.chenyunlong.qing.service.llm.service.parser.ccb;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.service.parser.BaseParserTest;
import cn.chenyunlong.qing.service.llm.service.parser.ccb.CcbExcelParser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Requires local sample bills")
class CcbExcelParserTest extends BaseParserTest {

    /**
     * 解析器基础冒烟测试：仅验证可解析且关键字段非空。
     */
    @Test
    void testParseSmoke() throws Exception {
        if (!resourceExists("ccb/excel/ccb_test.xls")) {
            return;
        }

        try (InputStream is = getResourceAsStream("ccb/excel/ccb_test.xls")) {
            CcbExcelParser parser = new CcbExcelParser();
            ParseResult result = parser.parse(is, "ccb_test.xls");
            List<UnifiedDraftRecord> records = result.getRecords();

            assertFalse(records.isEmpty());
            UnifiedDraftRecord first = records.get(0);
            assertNotNull(first.getTransactionTime());
            assertNotNull(first.getAmount());
        }
    }
}

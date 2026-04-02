package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class WechatParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        WechatParser parser = new WechatParser();
        InputStream is = getFirstFileInDir("微信账单", ".xlsx");
        if (is == null) return;
        
        try (is) {
            ParseResult parseResult = parser.parse(is, "wechat_test.xlsx");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("微信解析条数: " + records.size());
            assertFalse(records.isEmpty());
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}
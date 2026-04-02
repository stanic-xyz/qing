package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PingAnParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        PingAnParser parser = new PingAnParser();
        InputStream is = getFirstFileInDir("银行流水记录/平安银行", ".xlsx");
        if (is == null) return;
        
        try (is) {
            ParseResult parseResult = parser.parse(is, "pingan_test.xlsx");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("平安银行解析条数: " + records.size());
            assertFalse(records.isEmpty());
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}
package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class BocomCreditParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        BocomCreditParser parser = new BocomCreditParser();
        InputStream is = getFirstFileInDir("银行流水记录/交通银行信用卡", ".pdf");
        if (is == null) return;
        
        try (is) {
            ParseResult parseResult = parser.parse(is, "bocom_test.pdf");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("交通银行信用卡解析条数: " + records.size());
            assertFalse(records.isEmpty());
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}
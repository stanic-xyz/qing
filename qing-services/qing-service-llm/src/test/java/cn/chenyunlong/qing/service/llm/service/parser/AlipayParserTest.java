package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AlipayParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        AlipayParser parser = new AlipayParser();
        InputStream is = getFirstFileInDir("支付宝账单", ".csv");
        if (is == null) return;
        
        try (is) {
            List<TransactionRecord> records = parser.parse(is, "alipay_test.csv");
            System.out.println("支付宝解析条数: " + records.size());
            assertFalse(records.isEmpty());
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}
package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CiticCreditParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        if (!resourceExists("citic/citic_test.xls")) {
            System.out.println("⚠️ 找不到中信银行信用卡测试文件: mock/citic/citic_test.xls");
            System.out.println("  请将匿名化后的中信银行已出账单明细 XLS 文件放入 src/test/resources/mock/citic/citic_test.xls");
            return;
        }

        try (InputStream is = getResourceAsStream("citic/citic_test.xls")) {
            CiticCreditParser parser = new CiticCreditParser();
            ParseResult result = parser.parse(is, "citic_test.xls");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("中信银行信用卡解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("CITIC_CREDIT", parser.channelCode());

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }
}

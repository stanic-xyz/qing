package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PingAnParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        if (!resourceExists("pab/pab_test.xlsx")) {
            System.out.println("⚠️ 找不到平安银行测试文件: mock/pab/pab_test.xlsx");
            System.out.println("  请将匿名化后的平安银行账单 XLSX 文件放入 src/test/resources/mock/pab/pab_test.xlsx");
            return;
        }

        try (InputStream is = getResourceAsStream("pab/pab_test.xlsx")) {
            PingAnParser parser = new PingAnParser();
            ParseResult result = parser.parse(is, "pab_test.xlsx");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("平安银行解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("PINGAN", parser.channelCode());

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }
}

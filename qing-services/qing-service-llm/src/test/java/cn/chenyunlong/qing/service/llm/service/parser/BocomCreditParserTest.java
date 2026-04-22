package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BocomCreditParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        if (!resourceExists("bocom/pdf/bocom_test.pdf")) {
            System.out.println("⚠️ 找不到交通银行信用卡测试文件: mock/bocom/pdf/bocom_test.pdf");
            System.out.println("  请将匿名化后的交通银行信用卡电子账单 PDF 文件放入 src/test/resources/mock/bocom/pdf/bocom_test.pdf");
            return;
        }

        try (InputStream is = getResourceAsStream("bocom/pdf/bocom_test.pdf")) {
            BocomCreditParser parser = new BocomCreditParser();
            ParseResult result = parser.parse(is, "bocom_test.pdf");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("交通银行信用卡解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("BOCOM_CREDIT", parser.channelCode());

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }
}

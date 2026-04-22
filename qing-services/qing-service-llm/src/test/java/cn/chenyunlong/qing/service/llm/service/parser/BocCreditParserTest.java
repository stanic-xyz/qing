package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BocCreditParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        if (!resourceExists("boc/pdf/boc_test.pdf")) {
            System.out.println("⚠️ 找不到中国银行信用卡测试文件: mock/boc/pdf/boc_test.pdf");
            System.out.println("  请将匿名化后的中国银行信用卡账单 PDF 文件放入 src/test/resources/mock/boc/pdf/boc_test.pdf");
            return;
        }

        try (InputStream is = getResourceAsStream("boc/pdf/boc_test.pdf")) {
            BocCreditParser parser = new BocCreditParser();
            ParseResult result = parser.parse(is, "boc_test.pdf");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("中国银行信用卡解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("BOC_CREDIT", parser.channelCode());

            // 计算收支总额
            BigDecimal totalExpense = BigDecimal.ZERO;
            BigDecimal totalIncome = BigDecimal.ZERO;
            for (TransactionRecord r : records) {
                if ("EXPENSE".equals(r.getType().name())) {
                    totalExpense = totalExpense.add(r.getAmount());
                } else if ("INCOME".equals(r.getType().name())) {
                    totalIncome = totalIncome.add(r.getAmount());
                }
            }
            System.out.println("总支出: " + totalExpense + ", 总收入: " + totalIncome);

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }
}

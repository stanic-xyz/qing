package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlipayParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        if (!resourceExists("alipay/alipay_test.csv")) {
            System.out.println("⚠️ 找不到支付宝测试文件: mock/alipay/alipay_test.csv");
            System.out.println("  请将匿名化后的支付宝账单 CSV 文件放入 src/test/resources/mock/alipay/alipay_test.csv");
            return;
        }

        try (InputStream is = getResourceAsStream("alipay/alipay_test.csv")) {
            AlipayParser parser = new AlipayParser();
            ParseResult result = parser.parse(is, "alipay_test.csv");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("支付宝解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            // 验证第一条记录
            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getCounterparty(), "对手方不应为空");
            assertEquals("ALIPAY", parser.channelCode());

            // 打印分类统计
            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }
}

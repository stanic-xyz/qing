package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WechatParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        if (!resourceExists("wechat/wechat_test.xlsx")) {
            System.out.println("⚠️ 找不到微信测试文件: mock/wechat/wechat_test.xlsx");
            System.out.println("  请将匿名化后的微信账单 XLSX 文件放入 src/test/resources/mock/wechat/wechat_test.xlsx");
            return;
        }

        try (InputStream is = getResourceAsStream("wechat/wechat_test.xlsx")) {
            WechatParser parser = new WechatParser();
            ParseResult result = parser.parse(is, "wechat_test.xlsx");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("微信解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            // 基本字段验证
            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("WECHAT", parser.channelCode());

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }
}

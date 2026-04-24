package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CcbParserTest extends BaseParserTest {

    @Test
    public void testParseExcel() throws Exception {
        if (!resourceExists("ccb/excel/ccb_test.xls")) {
            System.out.println("找不到建设银行测试文件: mock/ccb/excel/ccb_test.xls");
            System.out.println("请将匿名化后的建设银行交易明细 XLS 文件放入 src/test/resources/mock/ccb/excel/ccb_test.xls");
            return;
        }

        try (InputStream is = getResourceAsStream("ccb/excel/ccb_test.xls")) {
            CcbParser parser = new CcbParser();
            ParseResult result = parser.parse(is, "ccb_test.xls");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("建设银行解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord first = records.getFirst();
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("CCB", parser.channelCode());

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            long transfer = records.stream().filter(r -> "TRANSFER".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔, 转账: " + transfer + " 笔");
        }
    }
}

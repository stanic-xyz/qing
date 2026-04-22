package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CmbParserTest extends BaseParserTest {

    @Test
    public void testParsePdf() throws Exception {
        if (!resourceExists("cmb/pdf/cmb_test.pdf")) {
            System.out.println("⚠️ 找不到招商银行测试文件: mock/cmb/pdf/cmb_test.pdf");
            System.out.println("  请将匿名化后的招商银行交易流水 PDF 文件放入 src/test/resources/mock/cmb/pdf/cmb_test.pdf");
            return;
        }

        try (InputStream is = getResourceAsStream("cmb/pdf/cmb_test.pdf")) {
            CmbParser parser = new CmbParser();
            ParseResult result = parser.parse(is, "cmb_test.pdf");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("招商银行解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord first = records.get(0);
            assertNotNull(first.getTransactionTime(), "交易时间不应为空");
            assertNotNull(first.getAmount(), "金额不应为空");
            assertNotNull(first.getType(), "收支类型不应为空");
            assertEquals("CMB", parser.channelCode());

            long income = records.stream().filter(r -> "INCOME".equals(r.getType().name())).count();
            long expense = records.stream().filter(r -> "EXPENSE".equals(r.getType().name())).count();
            System.out.println("收入: " + income + " 笔, 支出: " + expense + " 笔");
        }
    }

    @Test
    public void testParseTxt() throws Exception {
        if (!resourceExists("cmb/txt/cmb_test.txt")) {
            System.out.println("⚠️ 找不到招商银行 TXT 测试文件: mock/cmb/txt/cmb_test.txt");
            System.out.println("  请将匿名化后的招商银行交易流水 TXT 文件放入 src/test/resources/mock/cmb/txt/cmb_test.txt");
            return;
        }

        try (InputStream is = getResourceAsStream("cmb/txt/cmb_test.txt")) {
            CmbParser parser = new CmbParser();
            ParseResult result = parser.parse(is, "cmb_test.txt");
            List<TransactionRecord> records = result.getRecords();

            System.out.println("招商银行(TXT)解析条数: " + records.size());
            if (!records.isEmpty()) {
                records.stream().limit(5).forEach(System.out::println);
            }
        }
    }
}

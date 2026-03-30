package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JingdongParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        JingdongParser parser = new JingdongParser();
        // 查找那个包含 35 笔记录的测试文件 (因为有个文件是 0 笔记录)
        Path dir = Paths.get("src/test/resources/bills/理财信息/京东账单");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("613_20260317223821.csv"))
                .findFirst();

        if (testFile.isEmpty()) {
            System.out.println("找不到指定的京东测试文件，跳过测试");
            return;
        }

        try (InputStream is = Files.newInputStream(testFile.get())) {
            List<TransactionRecord> records = parser.parse(is, "jd_test.csv");
            System.out.println("京东解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");

            // 测试记录1：退款记录
            TransactionRecord record1 = records.get(0);
            assertNotNull(record1.getTransactionTime(), "交易时间不能为空");
            assertEquals("2025-03-02T22:09:02", record1.getTransactionTime().toString());
            assertEquals(new BigDecimal("999.00"), record1.getAmount(), "退款金额应解析正确");
            assertEquals("OTHER", record1.getType(), "退款(收/支 为空或非收支)类型应为OTHER");
            assertEquals("JINGDONG", record1.getChannel());
            assertEquals("SUCCESS", record1.getStatus(), "交易状态应解析正确");

            // 测试记录2：正常支出
            TransactionRecord record2 = records.get(1);
            assertEquals("2025-03-02T22:07:51", record2.getTransactionTime().toString());
            assertEquals(new BigDecimal("1029.00"), record2.getAmount(), "支出金额应解析正确");
            assertEquals("EXPENSE", record2.getType(), "支出类型应为EXPENSE");

            // 测试记录4：还款带备注
            TransactionRecord record4 = records.get(3);
            assertEquals("2024-10-24T10:27:07", record4.getTransactionTime().toString());
            assertEquals(new BigDecimal("99.00"), record4.getAmount());
            assertEquals("还款至11月账单", record4.getRemark(), "备注应解析正确");

            System.out.println("--- 打印前5条记录 ---");
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}

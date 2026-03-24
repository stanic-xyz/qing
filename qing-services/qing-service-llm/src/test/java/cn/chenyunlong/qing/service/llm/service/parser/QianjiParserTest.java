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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QianjiParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        QianjiParser parser = new QianjiParser();
        Path dir = Paths.get("src/test/resources/bills/理财信息/钱迹账单");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("QianJi_日常账本_2026-03-17_214327.csv"))
                .findFirst();

        if (testFile.isEmpty()) {
            System.out.println("找不到指定的钱迹测试文件，跳过测试");
            return;
        }

        try (InputStream is = Files.newInputStream(testFile.get())) {
            List<TransactionRecord> records = parser.parse(is, "qianji_test.csv");
            System.out.println("钱迹解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");

            // 第一条记录：转账
            // "qj1773751412791103771","2026-03-17 20:43:16","其它",,"转账","11.98","CNY","建设银行","中信银行信用卡","云闪付App","","","","stanic",,,,""
            TransactionRecord record1 = records.getFirst();
            assertEquals("2026-03-17T20:43:16", record1.getTransactionTime().toString());
            assertEquals("其它", record1.getCategory());
            assertEquals("", record1.getSubCategory());
            assertEquals("TRANSFER", record1.getType());
            assertEquals(new BigDecimal("11.98"), record1.getAmount());
            assertEquals("建设银行", record1.getAccountName());
            assertEquals("中信银行信用卡", record1.getCounterparty());
            assertEquals("云闪付App", record1.getRemark());

            // 第二条记录：支出
            // "qj1773751339517122671","2026-03-17 20:40:23","餐饮","水果","支出","23.79","CNY","交通银行信用卡",,"番茄，小台芒，火锅面","","","","stanic",,,,""
            TransactionRecord record2 = records.get(1);
            assertEquals("2026-03-17T20:40:23", record2.getTransactionTime().toString());
            assertEquals("餐饮", record2.getCategory());
            assertEquals("水果", record2.getSubCategory());
            assertEquals("EXPENSE", record2.getType());
            assertEquals(new BigDecimal("23.79"), record2.getAmount());
            assertEquals("交通银行信用卡", record2.getAccountName());
            assertEquals("番茄，小台芒，火锅面", record2.getRemark());

            records.stream().limit(5).forEach(System.out::println);
        }
    }
}

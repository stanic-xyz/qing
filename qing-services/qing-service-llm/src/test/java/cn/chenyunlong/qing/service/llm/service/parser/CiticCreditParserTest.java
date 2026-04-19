package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CiticCreditParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        CiticCreditParser parser = new CiticCreditParser();
        Path dir = Paths.get("src/test/resources/bills/理财信息/银行流水记录/中信银行信用卡");
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            System.out.println("目录不存在，跳过测试: " + dir.toAbsolutePath());
            return;
        }
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("已出账单明细-2025-04-17至2025-05-16.xls"))
                .findFirst();

        if (testFile.isEmpty()) {
            System.out.println("找不到指定的中信测试文件");
            return;
        }
        
        try (InputStream is = Files.newInputStream(testFile.get())) {
            ParseResult parseResult = parser.parse(is, "citic_test.xls");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("中信银行信用卡解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");
            
            // 总额校验
            BigDecimal totalExpense = BigDecimal.ZERO;
            BigDecimal totalIncome = BigDecimal.ZERO;
            for (TransactionRecord record : records) {
                if ("EXPENSE".equals(record.getType())) {
                    totalExpense = totalExpense.add(record.getAmount());
                } else if ("INCOME".equals(record.getType())) {
                    totalIncome = totalIncome.add(record.getAmount());
                }
            }
            System.out.println("总支出: " + totalExpense);
            System.out.println("总收入: " + totalIncome);
            
            // 验证第一条记录
            TransactionRecord record1 = records.get(0);
            assertEquals("2025-05-13T00:00", record1.getTransactionTime().toString());
            assertEquals("EXPENSE", record1.getType());
            assertEquals(new BigDecimal("1.00"), record1.getAmount());
            assertEquals("美团支付－美团骑行", record1.getMerchant());
            
            // 验证财付通记录
            TransactionRecord record8 = records.get(7);
            assertEquals("2025-05-01T00:00", record8.getTransactionTime().toString());
            assertEquals("EXPENSE", record8.getType());
            assertEquals(new BigDecimal("169.00"), record8.getAmount());
            assertEquals("财付通－重庆市以纯服装店", record8.getMerchant());
            
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}
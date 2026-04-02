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

public class BocCreditParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        BocCreditParser parser = new BocCreditParser();
        Path dir = Paths.get("src/test/resources/bills/理财信息/银行流水记录/中国银行信用卡");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("2023年08月账单补制.PDF"))
                .findFirst();

        if (testFile.isEmpty()) {
            System.out.println("找不到指定的中国银行测试文件");
            return;
        }

        try (InputStream is = Files.newInputStream(testFile.get())) {
            ParseResult parseResult = parser.parse(is, "boc_test.pdf");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("中国银行信用卡解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");

            // 验证笔数，根据前面调试工具提取的文本，应当是15笔交易
            assertEquals(15, records.size(), "交易笔数应该为 15");

            // 计算总额 (支出 - 收入)
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
            // 净支出 = 总支出 - 总收入。账单上期欠款103.86，本期存入103.86，本期支出1364.81，所以本期新增应还 1364.81
            assertEquals(new BigDecimal("1364.81"), totalExpense, "总支出金额应为 1364.81");
            assertEquals(new BigDecimal("103.86"), totalIncome, "总收入金额应为 103.86");

            // 验证第一条多行记录：07/03 07/04 2688 支付宝-重庆城市通卡支付 有限责任公司CHN 1.03
            TransactionRecord record1 = records.get(0);
            assertEquals("2023-07-03T00:00", record1.getTransactionTime().toString());
            assertEquals("EXPENSE", record1.getType());
            assertEquals(new BigDecimal("1.03"), record1.getAmount());
            assertEquals("支付宝-重庆城市通卡支付 有限责任公司", record1.getMerchant());

            // 验证第三条单行记录：07/08 07/09 2688 微信-JetBrainsCHN 1253.38
            TransactionRecord record3 = records.get(2);
            assertEquals("2023-07-08T00:00", record3.getTransactionTime().toString());
            assertEquals("EXPENSE", record3.getType());
            assertEquals(new BigDecimal("1253.38"), record3.getAmount());
            assertEquals("微信-JetBrains", record3.getMerchant());

            // 验证代付（收入）：07/19 07/19 2688 代付 103.86
            // 根据日志输出，这条在索引 6
            TransactionRecord record6 = records.get(6);
            assertEquals("2023-07-19T00:00", record6.getTransactionTime().toString());
            assertEquals("INCOME", record6.getType());
            assertEquals(new BigDecimal("103.86"), record6.getAmount());
            assertEquals("代付", record6.getMerchant());

            records.stream().limit(5).forEach(System.out::println);
        }
    }
}

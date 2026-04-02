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

public class CcbParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        CcbParser parser = new CcbParser();
        Path dir = Paths.get("src/test/resources/bills/理财信息/银行流水记录/建设银行/pdf");
        Optional<Path> testFile = Files.walk(dir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".pdf"))
                .findFirst();

        if (testFile.isEmpty()) {
            System.out.println("找不到指定的建设银行测试文件");
            return;
        }

        try (InputStream is = Files.newInputStream(testFile.get())) {
            ParseResult parseResult = parser.parse(is, "ccb_test.pdf");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("建设银行解析条数: " + records.size());
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
            // 真实测试账单前31条记录大概支出与收入，如果有精确对账单可以写死具体值
            // 这里我们主要断言解析出来的非零笔数
            
            // 验证第一条：1 转账存入 20150904 20.00 20.00 四川省分行营管部 6217003810043311771/张 婷
            TransactionRecord record1 = records.get(0);
            assertEquals("2015-09-04T00:00", record1.getTransactionTime().toString());
            assertEquals("INCOME", record1.getType());
            assertEquals(new BigDecimal("20.00"), record1.getAmount());
            assertEquals("转账存入", record1.getCategory());
            assertEquals("四川省分行营管部", record1.getMerchant());
            assertEquals("6217003810043311771/张婷", record1.getCounterparty());
            
            // 验证跨行记录：14 代扣学杂 20150907 -7,050.00 154.00 代扣学杂 10151088533622959900801222/成都理\n工大学学费代收户
            TransactionRecord record14 = records.get(13);
            assertEquals("2015-09-07T00:00", record14.getTransactionTime().toString());
            assertEquals("EXPENSE", record14.getType());
            assertEquals(new BigDecimal("7050.00"), record14.getAmount());
            assertEquals("代扣学杂", record14.getCategory());
            assertEquals("代扣学杂", record14.getMerchant());
            assertEquals("10151088533622959900801222/成都理工大学学费代收户", record14.getCounterparty());
            
            records.stream().limit(5).forEach(System.out::println);
        }
    }
}
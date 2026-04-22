package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

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

public class CmbParserTest extends BaseParserTest {

    @Test
    public void testParse() throws Exception {
        CmbParser parser = new CmbParser();

        ClassPathResource pathResource = new ClassPathResource("mock/cmb/cmb_bill.pdf");
        if (!pathResource.exists() || !pathResource.isReadable()) {
            System.out.println("找不到指定的招商测试文件,或者文件不可读");
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "cmb_bill.pdf");
        List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("招商银行解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");

            // 验证第一条：2019-12-06 CNY 1,000.00 1,000.00 网联收款
            TransactionRecord record1 = records.getFirst();
            assertEquals("2019-12-06T00:00", record1.getTransactionTime().toString());
            assertEquals(TrasactionType.INCOME, record1.getType());
            assertEquals(new BigDecimal("1000.00"), record1.getAmount());
            System.out.println("第一条对方信息: " + record1.getCounterparty());

            // 验证第二条：2019-12-06 CNY -1,000.00 0.00 活期转入朝朝盈
            TransactionRecord record2 = records.get(1);
            assertEquals("2019-12-06T00:00", record2.getTransactionTime().toString());
            assertEquals(TrasactionType.EXPENSE, record2.getType());
            assertEquals(new BigDecimal("1000.00"), record2.getAmount());
            System.out.println("第二条对方信息: " + record2.getCounterparty());

            // 验证第六条：2020-05-07 CNY -347.54 653.84 快捷支付
            TransactionRecord record5 = records.get(5);
            assertEquals("2020-05-07T00:00", record5.getTransactionTime().toString());
            assertEquals(TrasactionType.EXPENSE, record5.getType());
            assertEquals(new BigDecimal("347.54"), record5.getAmount());

            records.stream().limit(5).forEach(System.out::println);
        }
    }
}

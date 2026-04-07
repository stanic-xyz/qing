package cn.chenyunlong.qing.service.llm.service.parser.ccb;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 建设银行Excel解析器单元测试
 */
@DisplayName("建设银行Excel解析器测试")
class CcbExcelParserTest {

    private CcbExcelParser parser;

    @BeforeEach
    void setUp() {
        parser = new CcbExcelParser();
    }

    @Test
    @DisplayName("测试Excel文件解析")
    void testParseExcel() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/excel/test.xls");

        if (!pathResource.exists()) {
            System.out.println("⚠️ 找不到建设银行Excel测试文件: mock/ccb/excel/test.xls");
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.xls");
            List<TransactionRecord> records = parseResult.getRecords();

            System.out.println("📊 建设银行Excel解析条数: " + records.size());
            assertFalse(records.isEmpty(), "解析结果不应为空");

            // 总额校验
            BigDecimal totalExpense = BigDecimal.ZERO;
            BigDecimal totalIncome = BigDecimal.ZERO;
            for (TransactionRecord record : records) {
                if (TrasactionType.EXPENSE.equals(record.getType())) {
                    totalExpense = totalExpense.add(record.getAmount());
                } else if (TrasactionType.INCOME.equals(record.getType())) {
                    totalIncome = totalIncome.add(record.getAmount());
                }
            }
            System.out.println("💰 总支出: " + totalExpense);
            System.out.println("💰 总收入: " + totalIncome);

            // 验证第一条记录
            // 1 转账存入 20150904 20.00 20.00 四川省分行营管部 6217003810043311771/张婷
            TransactionRecord record1 = records.getFirst();
            assertNotNull(record1.getTransactionTime(), "交易时间不应为空");
            assertEquals("2015-09-04T00:00", record1.getTransactionTime().toString());
            assertEquals(TrasactionType.INCOME, record1.getType(), "转账存入应为收入");
            assertEquals(new BigDecimal("20.00"), record1.getAmount(), "金额应为20.00");
            assertEquals("四川省分行营管部", record1.getMerchant(), "商户应为四川省分行营管部");
            assertEquals("建设银行", record1.getAccountName(), "账户名称应为建设银行");
            assertEquals(new BigDecimal("20.00"), record1.getBalance(), "余额应为20.00");

            System.out.println("✅ 第一条记录验证通过:");
            System.out.println("  - 时间: " + record1.getTransactionTime());
            System.out.println("  - 类型: " + record1.getType());
            System.out.println("  - 金额: " + record1.getAmount());
            System.out.println("  - 商户: " + record1.getMerchant());
            System.out.println("  - 余额: " + record1.getBalance());

            // 打印前5条记录
            System.out.println("\n📋 前5条记录:");
            records.stream().limit(5).forEach(record ->
                    System.out.println("  " + record.getTransactionTime() + " | " +
                            record.getType() + " | " +
                            record.getAmount() + " | " +
                            record.getMerchant())
            );
        }
    }

    @Test
    @DisplayName("测试Excel解析-支出记录")
    void testParseExpenseRecord() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/excel/test.xls");

        if (!pathResource.exists()) {
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.xls");
            List<TransactionRecord> records = parseResult.getRecords();

            // 验证第14条记录（索引13）
            // 14 代扣学杂 20150907 -7,050.00 154.00 代扣学杂 10151088533622959900801222/成都理工大学学费代收户
            if (records.size() > 13) {
                TransactionRecord record14 = records.get(13);
                assertNotNull(record14.getTransactionTime(), "交易时间不应为空");
                assertEquals("2015-09-07T00:00", record14.getTransactionTime().toString());
                assertEquals(TrasactionType.EXPENSE, record14.getType(), "代扣学杂应为支出");
                assertEquals(new BigDecimal("7050.00"), record14.getAmount(), "金额应为7050.00");
                assertEquals(new BigDecimal("154.00"), record14.getBalance(), "余额应为154.00");

                System.out.println("✅ 第14条记录验证通过:");
                System.out.println("  - 时间: " + record14.getTransactionTime());
                System.out.println("  - 类型: " + record14.getType());
                System.out.println("  - 金额: " + record14.getAmount());
                System.out.println("  - 余额: " + record14.getBalance());
                System.out.println("  - 商户: " + record14.getMerchant());
                System.out.println("  - 备注: " + record14.getRemark());
            }
        }
    }

    @Test
    @DisplayName("测试Excel解析-数据完整性")
    void testParseDataIntegrity() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/excel/test.xls");

        if (!pathResource.exists()) {
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.xls");
            List<TransactionRecord> records = parseResult.getRecords();

            // 验证所有记录的必需字段
            for (TransactionRecord record : records) {
                assertNotNull(record.getTransactionTime(), "交易时间不应为空");
                assertNotNull(record.getAmount(), "交易金额不应为空");
                assertNotNull(record.getType(), "交易类型不应为空");
                assertEquals("建设银行", record.getAccountName(), "账户名称应为建设银行");
                assertNotNull(record.getAccountType(), "账户类型不应为空");
                assertNotNull(record.getReconciliationStatus(), "对账状态不应为空");
                assertNotNull(record.getStatus(), "交易状态不应为空");

                // 验证金额不为负数（已取绝对值）
                assertTrue(record.getAmount().compareTo(BigDecimal.ZERO) >= 0,
                        "金额应为非负数: " + record.getAmount());
            }

            System.out.println("✅ 数据完整性验证通过，共验证 " + records.size() + " 条记录");
        }
    }

    @Test
    @DisplayName("测试支持的扩展名")
    void testSupportedExtensions() {
        List<String> stringList = parser.supportedFileExtensions();

        assertTrue(CollUtil.isNotEmpty(stringList), "支持的扩展名不应为空");
        assertTrue(stringList.contains("xls"), "应支持xls格式");
        assertTrue(stringList.contains("xlsx"), "应支持xlsx格式");
        System.out.println("✅ 支持的扩展名: " + stringList);
    }

    @Test
    @DisplayName("测试渠道代码")
    void testChannelCode() {
        String channelCode = parser.channelCode();
        assertEquals("CCB", channelCode, "渠道代码应为CCB");
        System.out.println("✅ 渠道代码: " + channelCode);
    }

    @Test
    @DisplayName("测试空文件解析")
    void testParseEmptyFile() throws Exception {
        // 创建一个空的xlsx文件进行测试
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/excel/empty.xlsx");

        if (!pathResource.exists()) {
            System.out.println("⚠️ 找不到空Excel测试文件，跳过此测试");
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "empty.xlsx");
            List<TransactionRecord> records = parseResult.getRecords();

            assertNotNull(records, "解析结果不应为null");
            assertTrue(records.isEmpty(), "空文件解析结果应为空列表");
            System.out.println("✅ 空文件解析测试通过");
        }
    }
}

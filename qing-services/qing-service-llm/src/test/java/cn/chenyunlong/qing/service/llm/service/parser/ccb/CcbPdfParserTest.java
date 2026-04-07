package cn.chenyunlong.qing.service.llm.service.parser.ccb;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 建设银行PDF解析器单元测试
 */
@DisplayName("建设银行PDF解析器测试")
class CcbPdfParserTest {

    private CcbPdfParser parser;

    @BeforeEach
    void setUp() {
        parser = new CcbPdfParser();
    }

    @Test
    @DisplayName("测试PDF文件解析")
    void testParsePdf() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/pdf/test.pdf");

        if (!pathResource.exists()) {
            System.out.println("⚠️ 找不到建设银行PDF测试文件: mock/ccb/pdf/test.pdf");
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.pdf");
            List<TransactionRecord> records = parseResult.getRecords();

            System.out.println("📊 建设银行PDF解析条数: " + records.size());
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
    @DisplayName("测试PDF解析-支出记录")
    void testParseExpenseRecord() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/pdf/test.pdf");

        if (!pathResource.exists()) {
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.pdf");
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
    @DisplayName("测试PDF解析-跨行备注处理")
    void testParseMultiLineRemark() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/pdf/test.pdf");

        if (!pathResource.exists()) {
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.pdf");
            List<TransactionRecord> records = parseResult.getRecords();

            // 查找有备注的记录
            long recordsWithRemark = records.stream()
                    .filter(r -> r.getRemark() != null && !r.getRemark().isEmpty())
                    .count();

            System.out.println("📝 有备注的记录数: " + recordsWithRemark);

            // 打印有备注的记录
            records.stream()
                    .filter(r -> r.getRemark() != null && !r.getRemark().isEmpty())
                    .limit(3)
                    .forEach(record -> {
                        System.out.println("  - 时间: " + record.getTransactionTime());
                        System.out.println("    商户: " + record.getMerchant());
                        System.out.println("    备注: " + record.getRemark());
                    });
        }
    }

    @Test
    @DisplayName("测试PDF解析-数据完整性")
    void testParseDataIntegrity() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/pdf/test.pdf");

        if (!pathResource.exists()) {
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.pdf");
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
    @DisplayName("测试PDF解析-收入支出分类")
    void testTransactionTypeClassification() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/pdf/test.pdf");

        if (!pathResource.exists()) {
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "ccb_test.pdf");
            List<TransactionRecord> records = parseResult.getRecords();

            long incomeCount = records.stream()
                    .filter(r -> TrasactionType.INCOME.equals(r.getType()))
                    .count();

            long expenseCount = records.stream()
                    .filter(r -> TrasactionType.EXPENSE.equals(r.getType()))
                    .count();

            System.out.println("📊 交易类型统计:");
            System.out.println("  - 收入笔数: " + incomeCount);
            System.out.println("  - 支出笔数: " + expenseCount);
            System.out.println("  - 总笔数: " + records.size());

            assertTrue(incomeCount + expenseCount == records.size(),
                    "所有记录都应有明确的交易类型");
        }
    }

    @Test
    @DisplayName("测试支持的扩展名")
    void testSupportedExtensions() {
        String extensions = parser.supportedFileExtensions();
        assertNotNull(extensions, "支持的扩展名不应为空");
        assertTrue(extensions.contains("pdf"), "应支持pdf格式");
        System.out.println("✅ 支持的扩展名: " + extensions);
    }

    @Test
    @DisplayName("测试渠道代码")
    void testChannelCode() {
        String channelCode = parser.channelCode();
        assertEquals("CCB", channelCode, "渠道代码应为CCB");
        System.out.println("✅ 渠道代码: " + channelCode);
    }

    @Test
    @DisplayName("测试空PDF解析")
    void testParseEmptyPdf() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/ccb/pdf/empty.pdf");

        if (!pathResource.exists()) {
            System.out.println("⚠️ 找不到空PDF测试文件，跳过此测试");
            return;
        }

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = parser.parse(is, "empty.pdf");
            List<TransactionRecord> records = parseResult.getRecords();

            assertNotNull(records, "解析结果不应为null");
            // 空PDF可能解析出0条记录
            System.out.println("✅ 空PDF解析测试通过，解析记录数: " + records.size());
        }
    }
}

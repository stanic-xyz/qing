package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.FieldMappingRule;
import cn.chenyunlong.qing.service.llm.dto.parser.MetadataRule;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class DynamicFileParserTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testParseAlipayCsv() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/alipay/alipay_test.csv");
        Assertions.assertTrue(pathResource.exists() && pathResource.isReadable(), "支付宝测试文件不存在或者不可读！");

        // 1. 构建模拟的动态解析器配置 (针对支付宝 CSV)
        ParserConfig config = new ParserConfig();
        config.setName("动态支付宝解析器测试");
        config.setChannel("ALIPAY");
        config.setFileType("CSV");
        config.setEncoding("GBK");
        config.setSkipRows(25); // 支付宝账单从第26行（索引25）开始是真实数据

        // 构建元数据规则 (第8行，索引7："共26笔记录")
        MetadataRule metaRule1 = new MetadataRule();
        metaRule1.setTargetField("recordCount");
        metaRule1.setRowIndex(7);
        metaRule1.setColIndex(0);
        metaRule1.setRegex("(\\d+)"); // 提取数字
        config.setMetadataRules(objectMapper.writeValueAsString(Arrays.asList(metaRule1)));

        // 构建字段映射规则
        FieldMappingRule fTime = new FieldMappingRule();
        fTime.setTargetField("transactionTime");
        fTime.setSourceIndex(0);
        fTime.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FieldMappingRule fAmount = new FieldMappingRule();
        fAmount.setTargetField("amount");
        fAmount.setSourceIndex(6);
        fAmount.setCleanRules(Arrays.asList("REMOVE_RMB", "REMOVE_COMMAS"));

        FieldMappingRule fType = new FieldMappingRule();
        fType.setTargetField("type");
        fType.setSourceIndex(5);

        FieldMappingRule fCounterparty = new FieldMappingRule();
        fCounterparty.setTargetField("counterparty");
        fCounterparty.setSourceIndex(2);

        FieldMappingRule fMerchant = new FieldMappingRule();
        fMerchant.setTargetField("merchant");
        fMerchant.setSourceIndex(4);

        FieldMappingRule fFundSource = new FieldMappingRule();
        fFundSource.setTargetField("fundSource");
        fFundSource.setSourceIndex(7);
        fFundSource.setCleanRules(Arrays.asList("TRIM", "REMOVE_TABS"));

        FieldMappingRule fExtPayment = new FieldMappingRule();
        fExtPayment.setTargetField("extData.paymentMethod");
        fExtPayment.setSourceIndex(7);
        fExtPayment.setCleanRules(Arrays.asList("TRIM", "REMOVE_TABS"));

        FieldMappingRule fExtOrder = new FieldMappingRule();
        fExtOrder.setTargetField("extData.merchantOrderNo");
        fExtOrder.setSourceIndex(10);
        fExtOrder.setCleanRules(Arrays.asList("TRIM", "REMOVE_TABS"));

        config.setFieldMappingRules(objectMapper.writeValueAsString(Arrays.asList(
                fTime, fAmount, fType, fCounterparty, fMerchant, fFundSource, fExtPayment, fExtOrder
        )));

        // 2. 执行解析
        DynamicFileParser parser = new DynamicFileParser(config);

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult result = parser.parse(is, "alipay_test.csv");
            List<TransactionRecord> records = result.getRecords();

            // 3. 断言验证元数据
            Assertions.assertNotNull(result.getMetadata());
            Assertions.assertEquals(26, result.getMetadata().getRecordCount(), "元数据中的记录笔数提取应为26");
            Assertions.assertNotNull(result.getMetadata().getStartTime(), "应自动推算出起始时间");

            // 4. 断言验证交易记录
            Assertions.assertFalse(records.isEmpty());
            System.out.println("动态解析出的记录数：" + records.size());

            // 取索引为 7 的记录 (即原测试中的第8条数据：4.5元的停车费)
            TransactionRecord record = records.get(7);
            Assertions.assertNotNull(record);

            // 验证基础字段映射
            Assertions.assertEquals(0, BigDecimal.valueOf(4.5).compareTo(record.getAmount()));
            Assertions.assertEquals("EXPENSE", record.getType()); // 动态解析器应自动将"支出"映射为EXPENSE
            Assertions.assertEquals("川北医学院附属医院", record.getCounterparty());
            Assertions.assertEquals("停车缴费-渝C093D6-川北医学院附属医院茂源南路综合院区", record.getMerchant());

            // 验证资金来源与自动打标
            Assertions.assertEquals("交通银行信用卡(7581)&红包", record.getFundSource());
            Assertions.assertEquals("EXTERNAL", record.getFundType(), "包含信用卡字样，应自动推断为EXTERNAL资金");

            // 验证扩展数据 (JSON 映射)
            Assertions.assertNotNull(record.getOriginalData());
            String extDataStr = record.getOriginalData();
            Assertions.assertTrue(extDataStr.contains("\"paymentMethod\":\"交通银行信用卡(7581)&红包\""));
            Assertions.assertTrue(extDataStr.contains("\"merchantOrderNo\":\"301260305DGQHN6EE9Z42\""));

            System.out.println("成功验证单条动态解析结果: " + record);
        }
    }
}

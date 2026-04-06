package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.FieldMappingRule;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DynamicFileParserScriptTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static ScriptExecutorFactory newExecutorFactory() {
        ScriptExecutorFactory factory = new ScriptExecutorFactory();
        factory.init();
        return factory;
    }

    private static ParserConfig baseCsvConfig() {
        ParserConfig config = new ParserConfig();
        config.setName("script-test");
        Channel channel = new Channel();
        channel.setCode("ALIPAY");
        config.setChannel(channel);
        config.setFileType("CSV");
        config.setEncoding("UTF-8");
        config.setSkipRows(0);
        return config;
    }

    @Test
    public void shouldExecuteGroovyScriptAndCoerceType() throws Exception {
        ParserConfig config = baseCsvConfig();

        FieldMappingRule fTime = new FieldMappingRule();
        fTime.setTargetField("transactionTime");
        fTime.setSourceIndex(0);
        fTime.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FieldMappingRule fAmount = new FieldMappingRule();
        fAmount.setTargetField("amount");
        fAmount.setSourceIndex(1);
        fAmount.setScriptEnabled(true);
        fAmount.setScriptLanguage("groovy");
        fAmount.setScriptRule("return new BigDecimal(value).multiply(new BigDecimal('2'))");

        config.setFieldMappingRules(objectMapper.writeValueAsString(Arrays.asList(fTime, fAmount)));

        String csv = "2026-01-01 00:00:00,12.34\n";
        DynamicFileParser parser = new DynamicFileParser(config, newExecutorFactory(), true);
        ParseResult result = parser.parse(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), "test.csv");

        Assertions.assertEquals(1, result.getRecords().size());
        TransactionRecord record = result.getRecords().get(0);
        Assertions.assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0, 0), record.getTransactionTime());
        Assertions.assertEquals(0, new BigDecimal("24.68").compareTo(record.getAmount()));
    }

    @Test
    public void shouldNotExecuteScriptWhenDisabled() throws Exception {
        ParserConfig config = baseCsvConfig();

        FieldMappingRule fTime = new FieldMappingRule();
        fTime.setTargetField("transactionTime");
        fTime.setSourceIndex(0);
        fTime.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FieldMappingRule fAmount = new FieldMappingRule();
        fAmount.setTargetField("amount");
        fAmount.setSourceIndex(1);
        fAmount.setScriptEnabled(false);
        fAmount.setScriptLanguage("groovy");
        fAmount.setScriptRule("return new BigDecimal('999')");

        config.setFieldMappingRules(objectMapper.writeValueAsString(Arrays.asList(fTime, fAmount)));

        String csv = "2026-01-01 00:00:00,12.34\n";
        DynamicFileParser parser = new DynamicFileParser(config, newExecutorFactory(), true);
        ParseResult result = parser.parse(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), "test.csv");

        Assertions.assertEquals(1, result.getRecords().size());
        TransactionRecord record = result.getRecords().get(0);
        Assertions.assertEquals(0, new BigDecimal("12.34").compareTo(record.getAmount()));
    }

    @Test
    public void shouldValidateScriptOutputTypeInStrictMode() throws Exception {
        ParserConfig config = baseCsvConfig();

        FieldMappingRule fTime = new FieldMappingRule();
        fTime.setTargetField("transactionTime");
        fTime.setSourceIndex(0);
        fTime.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FieldMappingRule fAmount = new FieldMappingRule();
        fAmount.setTargetField("amount");
        fAmount.setSourceIndex(1);
        fAmount.setScriptEnabled(true);
        fAmount.setScriptLanguage("groovy");
        fAmount.setScriptRule("return ['not', 'a', 'number']");

        config.setFieldMappingRules(objectMapper.writeValueAsString(Arrays.asList(fTime, fAmount)));

        String csv = "2026-01-01 00:00:00,12.34\n";
        DynamicFileParser parser = new DynamicFileParser(config, newExecutorFactory(), true);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                parser.parse(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), "test.csv"));
    }

    @Test
    public void shouldSupportLegacyMapScriptReturn() throws Exception {
        ParserConfig config = baseCsvConfig();

        FieldMappingRule fTime = new FieldMappingRule();
        fTime.setTargetField("transactionTime");
        fTime.setSourceIndex(0);
        fTime.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FieldMappingRule fAmount = new FieldMappingRule();
        fAmount.setTargetField("amount");
        fAmount.setSourceIndex(1);
        fAmount.setScriptEnabled(true);
        fAmount.setScriptLanguage("groovy");
        fAmount.setScriptRule("return [amount: new BigDecimal(value).negate(), type: 'EXPENSE']");

        config.setFieldMappingRules(objectMapper.writeValueAsString(Arrays.asList(fTime, fAmount)));

        String csv = "2026-01-01 00:00:00,12.34\n";
        DynamicFileParser parser = new DynamicFileParser(config, newExecutorFactory(), true);
        ParseResult result = parser.parse(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), "test.csv");

        Assertions.assertEquals(1, result.getRecords().size());
        TransactionRecord record = result.getRecords().get(0);
        Assertions.assertEquals(0, new BigDecimal("-12.34").compareTo(record.getAmount()));
        Assertions.assertEquals("EXPENSE", record.getType());
    }

    @Test
    public void shouldAllowScriptForExtDataField() throws Exception {
        ParserConfig config = baseCsvConfig();

        FieldMappingRule fTime = new FieldMappingRule();
        fTime.setTargetField("transactionTime");
        fTime.setSourceIndex(0);
        fTime.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FieldMappingRule fAmount = new FieldMappingRule();
        fAmount.setTargetField("amount");
        fAmount.setSourceIndex(1);

        FieldMappingRule fExt = new FieldMappingRule();
        fExt.setTargetField("extData.merchantOrderNo");
        fExt.setSourceIndex(2);
        fExt.setScriptEnabled(true);
        fExt.setScriptLanguage("groovy");
        fExt.setScriptRule("return value + '-X'");

        config.setFieldMappingRules(objectMapper.writeValueAsString(Arrays.asList(fTime, fAmount, fExt)));

        String csv = "2026-01-01 00:00:00,1.00,ORD123\n";
        DynamicFileParser parser = new DynamicFileParser(config, newExecutorFactory(), true);
        ParseResult result = parser.parse(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), "test.csv");

        List<TransactionRecord> records = result.getRecords();
        Assertions.assertEquals(1, records.size());
        Assertions.assertNotNull(records.get(0).getOriginalData());
        Assertions.assertTrue(records.get(0).getOriginalData().contains("\"merchantOrderNo\":\"ORD123-X\""));
    }
}

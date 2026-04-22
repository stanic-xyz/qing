package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.ext.AlipayExtData;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.math.BigDecimal;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AlipayParserTest extends BaseParserTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testParse() throws Exception {
        ClassPathResource pathResource = new ClassPathResource("mock/alipay/alipay_test.csv");

        if (!pathResource.exists() && pathResource.isReadable()) {
            System.out.println("找不到指定的支付宝测试文件");
            return;
        }

        AlipayParser alipayParser = new AlipayParser();

        try (InputStream is = pathResource.getInputStream()) {
            ParseResult parseResult = alipayParser.parse(is, "alipay_test.csv");
            List<TransactionRecord> records = parseResult.getRecords();
            System.out.println("支付宝解析条数: " + records.size());

            assertFalse(records.isEmpty());
            records.stream().limit(5).forEach(System.out::println);

            TransactionRecord transactionRecord = records.get(7);

            AlipayExtData extData = AlipayExtData.fromRaw(transactionRecord.getOriginalData(), objectMapper);

            Assertions.assertNotNull(transactionRecord);
            Assertions.assertNotNull(transactionRecord.getAmount());
            Assertions.assertNotNull(transactionRecord.getTransactionTime());
            Assertions.assertNotNull(transactionRecord.getCounterparty());

            Assertions.assertEquals(0, BigDecimal.valueOf(4.5).compareTo(transactionRecord.getAmount()));
            Assertions.assertEquals("交通银行信用卡(7581)&红包", extData.getPaymentMethod());
            Assertions.assertEquals("301260305DGQHN6EE9Z42", extData.getMerchantOrderNo());
            Assertions.assertEquals("2026030523001414751440848696", extData.getTradeOrderNo());
            Assertions.assertEquals("cwc***@163.com", extData.getCounterpartyAccount());
            Assertions.assertEquals("川北医学院附属医院", extData.getCounterpartyName());
            Assertions.assertEquals("停车缴费-渝C093D6-川北医学院附属医院茂源南路综合院区", extData.getGoodsDescription());
        }
    }
}

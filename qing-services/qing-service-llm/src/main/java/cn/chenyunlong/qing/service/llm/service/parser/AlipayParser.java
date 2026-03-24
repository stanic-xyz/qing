package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component("ALIPAY")
public class AlipayParser extends BaseFileParser {
    // 支付宝CSV编码为GBK
    private static final Charset GBK = Charset.forName("GBK");
    private static final DateTimeFormatter ALIPAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, GBK)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                // 支付宝CSV有许多非数据行，列数不足的跳过，真实数据至少11列以上
                if (line.length < 8) continue;

                String timeStr = line[0].trim();
                // 过滤表头及页脚（交易时间通常以年份如 20xx 开头）
                if (!timeStr.startsWith("20")) {
                    continue;
                }

                try {
                    // 表头对应关系:
                    // [0]交易时间, [1]交易分类, [2]交易对方, [3]对方账号, [4]商品说明, [5]收/支, [6]金额, [7]收/付款方式, [8]交易状态, [9]交易订单号, [10]商家订单号, [11]备注
                    TransactionRecord record = new TransactionRecord();
                    record.setChannel("ALIPAY");
                    record.setTransactionTime(LocalDateTime.parse(timeStr, ALIPAY_FORMAT));

                    record.setCategory(line[1].trim());
                    record.setCounterparty(line[2].trim());
                    record.setMerchant(line[4].trim());

                    // 状态: [8]
                    record.setStatus(mapStatus(line[8].trim()));

                    // 金额: [6]
                    String amountStr = line[6].trim().replace("¥", "").replace(",", "");
                    BigDecimal amount = new BigDecimal(amountStr);

                    // 收/支: [5]
                    String incomeExpense = line[5].trim();
                    if ("收入".equals(incomeExpense)) {
                        record.setType("INCOME");
                    } else if ("支出".equals(incomeExpense)) {
                        record.setType("EXPENSE");
                    } else {
                        record.setType("OTHER");
                    }

                    record.setAmount(amount);

                    // 备注: [11] (如果有的话)
                    if (line.length > 11) {
                        record.setRemark(line[11].trim());
                    }

                    record.setAccountName("支付宝");
                    record.setAccountType("WALLET");
                    record.setReconciliationStatus("PENDING");
                    record.setConfirmed(false);
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析支付宝账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return records;
    }

    private String mapStatus(String status) {
        return switch (status) {
            case "成功", "交易成功", "已完成", "已退款", "解冻成功" -> "SUCCESS";
            case "失败", "交易关闭" -> "FAILED";
            case "处理中", "退款中", "等待付款" -> "PENDING";
            default -> "UNKNOWN";
        };
    }
}

package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component("QIANJI")
public class QianjiParser extends BaseFileParser {
    private static final DateTimeFormatter QIANJI_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 8) continue;
                
                // "ID","时间","分类","二级分类","类型","金额","币种","账户1","账户2","备注"
                String timeStr = line[1].trim();
                // 钱迹时间通常是 20xx-xx-xx，如果不是数字开头或者是表头，跳过
                if (!timeStr.startsWith("20")) {
                    continue;
                }

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setChannel("QIANJI");
                    record.setTransactionTime(LocalDateTime.parse(timeStr, QIANJI_FORMAT));
                    record.setCategory(line[2].trim());
                    record.setSubCategory(line[3].trim());
                    String typeStr = line[4].trim(); // 类型：支出/收入/转账
                    if ("支出".equals(typeStr)) record.setType("EXPENSE");
                    else if ("收入".equals(typeStr)) record.setType("INCOME");
                    else if ("转账".equals(typeStr)) record.setType("TRANSFER");
                    record.setAmount(new BigDecimal(line[5].trim()));
                    // 账户1 是主要账户
                    record.setAccountName(line[7].trim());
                    // 账户2 可能是对手账户
                    if (line.length > 8 && !line[8].trim().isEmpty()) {
                        record.setCounterparty(line[8].trim());
                    }
                    if (line.length > 9) {
                        record.setRemark(line[9].trim());
                    }
                    record.setAccountType("WALLET"); // 钱迹账户类型需从账户表映射，此处暂用WALLET
                    record.setReconciliationStatus("PENDING");
                    record.setConfirmed(false);
                    record.setStatus("SUCCESS");
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析钱迹账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return records;
    }
}

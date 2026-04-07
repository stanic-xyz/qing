package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
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

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("JINGDONG")
public class JingdongParser extends BaseFileParser {


    public static final String CHANNEL_CODE = "JINGDONG";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    private static final DateTimeFormatter JD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("([\\d.]+)");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 5) continue;

                // 去除隐藏字符和制表符
                String timeStr = line[0].replaceAll("[^\\d\\- :]", "").trim();
                // 过滤表头及空行
                if (!timeStr.startsWith("20")) {
                    continue;
                }

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(LocalDateTime.parse(timeStr, JD_FORMAT));
                    // 真实京东账单列：交易时间,商户名称,交易说明,金额,收/付款方式,交易状态,收/支,交易分类,交易订单号,商家订单号,备注
                    record.setCounterparty(null);
                    record.setMerchant(line[2].trim()); // 交易说明

                    String typeStr = line[6].trim(); // 收/支
                    if ("支出".equals(typeStr)) record.setType(TrasactionType.EXPENSE);
                    else if ("收入".equals(typeStr)) record.setType(TrasactionType.INCOME);
                    else record.setType(TrasactionType.OTHER);

                    String rawAmountStr = line[3].trim().replace("¥", "").replace(",", "");
                    Matcher matcher = AMOUNT_PATTERN.matcher(rawAmountStr);
                    if (matcher.find()) {
                        record.setAmount(new BigDecimal(matcher.group(1)));
                    } else {
                        record.setAmount(BigDecimal.ZERO);
                    }

                    record.setStatus(null); // 交易状态
                    record.setRemark(line.length > 10 ? line[10].trim() : "");

                    record.setAccountName("京东");
                    record.setAccountType(AccountType.WALLET);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);
                    record.setRecordRole(RecordRoleEnum.TRACE);
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析京东账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private String mapStatus(String status) {
        if (status.contains("成功") || status.contains("已完成")) return "SUCCESS";
        if (status.contains("失败") || status.contains("关闭")) return "FAILED";
        return "PENDING";
    }
}

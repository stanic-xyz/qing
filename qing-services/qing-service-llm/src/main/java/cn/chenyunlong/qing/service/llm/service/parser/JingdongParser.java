package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("JINGDONG")
public class JingdongParser extends BaseFileParser {

    private static final DateTimeFormatter JD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("([\\d.]+)");

    @Override
    public String channelCode() {
        return "JINGDONG";
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 5) continue;

                // 去除隐藏字符和制表符
                String timeStr = line[0].replaceAll("[^\\d\\- :]", "").trim();
                if (!timeStr.startsWith("20")) continue;

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(LocalDateTime.parse(timeStr, JD_FORMAT));

                    // 交易说明/商户
                    String description = line.length > 2 ? line[2].trim() : "";
                    record.setMerchant(description);

                    // 对手方（京东的交易说明字段包含商户信息）
                    if (!description.isEmpty()) {
                        Counterparty cp = new Counterparty();
                        cp.setName(description);
                        record.setCounterparty(cp);
                    }

                    // 收/支
                    String typeStr = line.length > 6 ? line[6].trim() : "";
                    if ("支出".equals(typeStr)) {
                        record.setType(TrasactionType.EXPENSE);
                    } else if ("收入".equals(typeStr)) {
                        record.setType(TrasactionType.INCOME);
                    } else {
                        record.setType(TrasactionType.OTHER);
                    }

                    // 金额（列3）
                    String rawAmountStr = line.length > 3 ? line[3].trim().replace("¥", "").replace(",", "") : "0";
                    // 处理 "999.00(已全额退款)" 格式
                    Matcher matcher = AMOUNT_PATTERN.matcher(rawAmountStr);
                    if (matcher.find()) {
                        record.setAmount(new BigDecimal(matcher.group(1)));
                    } else {
                        record.setAmount(BigDecimal.ZERO);
                    }

                    // 交易状态（列5）
                    String statusStr = line.length > 5 ? line[5].trim() : "";
                    record.setStatus(mapStatus(statusStr));

                    // 分类（从交易分类列8）
                    String category = "";
                    if (line.length > 7) {
                        category = line[7].trim();
                        record.setSubCategory(category);
                    }

                    // 备注（列10）
                    String remark = line.length > 10 ? line[10].trim() : "";
                    record.setRemark(remark);

                    // 原始ID（订单号，列8）
                    if (line.length > 8 && !line[8].trim().isEmpty()) {
                        record.setOriginalId(line[8].trim().replace("\t", ""));
                    }

                    record.setAccountName("京东");
                    record.setAccountType(AccountType.WALLET);
                    record.setRecordRole(RecordRoleEnum.PRIMARY);
                    record.setFundSource("京东");
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析京东账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private TransactionStatusEnum mapStatus(String status) {
        if (status == null || status.isEmpty()) return TransactionStatusEnum.PENDING;
        if (status.contains("成功") || status.contains("已完成")) return TransactionStatusEnum.SUCCESS;
        if (status.contains("失败") || status.contains("关闭") || status.contains("取消")) return TransactionStatusEnum.FAILED;
        return TransactionStatusEnum.PENDING;
    }
}

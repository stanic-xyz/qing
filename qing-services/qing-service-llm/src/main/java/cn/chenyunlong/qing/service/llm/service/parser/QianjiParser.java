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

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("QIANJI")
public class QianjiParser extends BaseFileParser {

    private static final DateTimeFormatter QIANJI_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String channelCode() {
        return "QIANJI";
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 8) continue;

                // "ID","时间","分类","二级分类","类型","金额","币种","账户1","账户2","备注"
                String timeStr = line[1].trim();
                if (!timeStr.startsWith("20")) continue;

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(LocalDateTime.parse(timeStr, QIANJI_FORMAT));

                    // 一级分类（对应 subCategory）
                    String category = line[2].trim();
                    record.setSubCategory(category);

                    // 二级分类（对应 category）
                    String subCategory = line[3].trim();
                    record.setCategory(subCategory);

                    // 类型：支出/收入/转账
                    String typeStr = line[4].trim();
                    if ("支出".equals(typeStr)) {
                        record.setType(TrasactionType.EXPENSE);
                    } else if ("收入".equals(typeStr)) {
                        record.setType(TrasactionType.INCOME);
                    } else if ("转账".equals(typeStr)) {
                        record.setType(TrasactionType.TRANSFER);
                    } else {
                        record.setType(TrasactionType.OTHER);
                    }

                    // 金额
                    record.setAmount(new BigDecimal(line[5].trim()));

                    // 账户1（主要账户）
                    String account1 = line.length > 7 ? line[7].trim() : "";
                    record.setAccountName(account1);

                    // 账户2（对手账户）
                    String account2 = line.length > 8 ? line[8].trim() : "";
                    if (!account2.isEmpty() && !"/".equals(account2)) {
                        Counterparty cp = new Counterparty();
                        cp.setName(account2);
                        record.setCounterparty(cp);
                    }

                    // 备注
                    String remark = line.length > 9 ? line[9].trim() : "";
                    record.setRemark(remark);

                    // 原始ID
                    if (line.length > 0 && !line[0].trim().isEmpty()) {
                        record.setOriginalId(line[0].trim());
                    }

                    record.setAccountType(mapAccountType(account1));
                    record.setStatus(TransactionStatusEnum.SUCCESS);
                    record.setRecordRole(RecordRoleEnum.TRACE);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析钱迹账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private AccountType mapAccountType(String accountName) {
        if (accountName == null || accountName.isEmpty()) return AccountType.WALLET;
        String lower = accountName.toLowerCase();
        if (lower.contains("信用卡")) return AccountType.CREDIT;
        if (lower.contains("储蓄卡") || lower.contains("银行")) return AccountType.DEBIT;
        return AccountType.WALLET;
    }
}

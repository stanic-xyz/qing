package cn.chenyunlong.qing.service.llm.service.imports.qianji;

import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import cn.chenyunlong.qing.service.llm.service.parser.BaseFileParser;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
public class QianjiParser extends BaseFileParser {

    private static final DateTimeFormatter QIANJI_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String channelCode() {
        return "QIANJI";
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {

        Map<String, List<String>> categories = new HashMap<>();
        List<String> accountList = new ArrayList<>();

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

                    List<String> subCategoriesList = new ArrayList<>();

                    // 一级分类（对应 subCategory）
                    String category = line[2].trim();
                    if (StrUtil.isNotBlank(category)) {
                        subCategoriesList = categories.getOrDefault(category, new ArrayList<>());
                    }

                    categories.put(category, subCategoriesList);

                    // 类型：支出/收入/转账
                    String typeStr = line[4].trim();
                    switch (typeStr) {
                        case "支出" -> record.setTransactionType(TransactionType.EXPENSE);
                        case "收入" -> record.setTransactionType(TransactionType.INCOME);
                        case "转账" -> record.setTransactionType(TransactionType.TRANSFER);
                    }

                    // 金额
                    record.setAmount(new BigDecimal(line[5].trim()));

                    // 账户1（主要账户）
                    String account1 = line[7].trim();
                    record.setAccountName(account1);

                    if (StrUtil.isNotBlank(account1) && !CollUtil.contains(accountList, account1)) {
                        accountList.add(account1);
                    }

                    // 账户2（对手账户）
                    String account2 = line.length > 8 ? line[8].trim() : "";
                    if (StrUtil.isNotBlank(account2) && !CollUtil.contains(accountList, account1)) {
                        accountList.add(account2);
                    }

                    if (!account2.isEmpty() && !"/".equals(account2)) {
                        Counterparty cp = new Counterparty();
                        cp.setName(account2);
                        record.setCounterparty(cp);
                    }

                    // 备注
                    String remark = line.length > 9 ? line[9].trim() : "";
                    record.setDetail(remark);

                    // 原始ID
                    if (!line[0].trim().isEmpty()) {
                        // todo 原始id需要保留
                        // record.setOriginalId(line[0].trim());
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
        ParseResult parseResult = wrapResult(records);

        parseResult.setCategories(categories);
        parseResult.setAccountList(accountList);

        return parseResult;
    }

    private AccountType mapAccountType(String accountName) {
        if (accountName == null || accountName.isEmpty()) return AccountType.WALLET;
        String lower = accountName.toLowerCase();
        if (lower.contains("信用卡")) return AccountType.CREDIT;
        if (lower.contains("储蓄卡") || lower.contains("银行")) return AccountType.DEBIT;
        return AccountType.WALLET;
    }
}

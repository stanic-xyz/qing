package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("CCB")
public class CcbParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "CCB";
    private static final String ACCOUNT_NAME = "建设银行储蓄卡(5864)";

    private static final Pattern INTERNAL_TRANSFER_PATTERN = Pattern.compile(
            "(转账存入|转账支取|信用卡还款|余额宝|零钱通|转入|转出)");

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int headerRowIdx = -1;
            for (int i = 0; i < Math.min(15, sheet.getPhysicalNumberOfRows()); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;
                String val = getCellValueAsString(firstCell);
                if (val.contains("序号") && val.length() < 20) {
                    headerRowIdx = i;
                    break;
                }
            }
            if (headerRowIdx < 0) {
                log.warn("建设银行账单: 未找到表头行");
                return wrapResult(records);
            }

            Row headerRow = sheet.getRow(headerRowIdx);
            int seqCol = -1, summaryCol = -1, dateCol = -1, amountCol = -1;
            int balanceCol = -1, locationCol = -1, counterpartyCol = -1;
            for (int idx = 0; idx < headerRow.getLastCellNum(); idx++) {
                Cell cell = headerRow.getCell(idx);
                if (cell == null) continue;
                String headerVal = getCellValueAsString(cell);
                if (headerVal.contains("序号")) seqCol = idx;
                else if (headerVal.contains("摘要")) summaryCol = idx;
                else if (headerVal.contains("交易日期")) dateCol = idx;
                else if (headerVal.contains("交易金额")) amountCol = idx;
                else if (headerVal.contains("账户余额")) balanceCol = idx;
                else if (headerVal.contains("交易地点") || headerVal.contains("附言")) locationCol = idx;
                else if (headerVal.contains("对方账号") || headerVal.contains("对方户名")) counterpartyCol = idx;
            }

            for (int ri = headerRowIdx + 1; ri <= sheet.getLastRowNum(); ri++) {
                try {
                    Row row = sheet.getRow(ri);
                    if (row == null) continue;
                    String seq = getCellValueAsString(row.getCell(seqCol)).trim();
                    if (seq.isEmpty() || "nan".equals(seq)) continue;
                    String dateStr = getCellValueAsString(row.getCell(dateCol)).trim();
                    if (dateStr.isEmpty()) continue;
                    dateStr = normalizeDate(dateStr);
                    if (dateStr == null) continue;
                    LocalDateTime txTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String amountRaw = getCellValueAsString(row.getCell(amountCol)).replace(",", "").trim();
                    if (amountRaw.isEmpty() || "nan".equals(amountRaw)) continue;
                    BigDecimal amount = new BigDecimal(amountRaw);
                    String summary = getCellValueAsString(row.getCell(summaryCol)).trim();
                    if ("nan".equals(summary)) summary = "";
                    String counterpartyRaw = "";
                    if (counterpartyCol >= 0) {
                        counterpartyRaw = getCellValueAsString(row.getCell(counterpartyCol)).trim();
                        if ("nan".equals(counterpartyRaw)) counterpartyRaw = "";
                    }
                    String location = "";
                    if (locationCol >= 0) {
                        location = getCellValueAsString(row.getCell(locationCol)).trim();
                        if ("nan".equals(location)) location = "";
                    }
                    BigDecimal balance = null;
                    if (balanceCol >= 0) {
                        try {
                            String balStr = getCellValueAsString(row.getCell(balanceCol)).replace(",", "").trim();
                            if (!balStr.isEmpty() && !"nan".equals(balStr)) balance = new BigDecimal(balStr);
                        } catch (Exception ignored) {}
                    }
                    boolean isExpense = amount.compareTo(BigDecimal.ZERO) < 0;
                    BigDecimal absAmount = amount.abs();
                    boolean isInternal = isInternalTransfer(summary, counterpartyRaw);
                    String cpName = "";
                    if (!counterpartyRaw.isEmpty()) {
                        String[] parts = counterpartyRaw.split("/");
                        cpName = parts[parts.length - 1].trim();
                    }
                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(txTime);
                    record.setAmount(absAmount);
                    record.setType(isInternal ? TrasactionType.TRANSFER
                            : (isExpense ? TrasactionType.EXPENSE : TrasactionType.INCOME));
                    record.setSubCategory(classifySummary(summary));
                    if (!cpName.isEmpty()) {
                        Counterparty cp = new Counterparty();
                        cp.setName(cpName);
                        record.setCounterparty(cp);
                    }
                    String description = summary;
                    if (!location.isEmpty()) description = description + " " + location;
                    if (description.length() > 255) description = description.substring(0, 255);
                    record.setMerchant(description.trim());
                    if (balance != null) record.setBalance(balance.abs());
                    record.setOriginalId("ccb_" + ACCOUNT_NAME + "_" + seq);
                    record.setAccountName(ACCOUNT_NAME);
                    record.setAccountType(AccountType.DEBIT);
                    record.setRecordRole(RecordRoleEnum.PRIMARY);
                    record.setFundSource(ACCOUNT_NAME);
                    record.setStatus(TransactionStatusEnum.SUCCESS);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析建设银行流水失败: 第 {} 行, 错误: {}", ri, e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private String normalizeDate(String s) {
        s = s.trim().replace("/", "");
        if (s.matches("\\d{8}")) {
            try {
                java.time.LocalDate d = java.time.LocalDate.of(
                        Integer.parseInt(s.substring(0, 4)),
                        Integer.parseInt(s.substring(4, 6)),
                        Integer.parseInt(s.substring(6, 8)));
                return d.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception ignored) {}
        }
        for (String fmt : new String[]{"yyyyMMdd", "yyyy-MM-dd"}) {
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(s.substring(0, Math.min(s.length(), 10)),
                        java.time.format.DateTimeFormatter.ofPattern(fmt));
                return d.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception ignored) {}
        }
        return null;
    }

    private boolean isInternalTransfer(String summary, String counterpartyRaw) {
        if (summary == null || summary.isEmpty()) return false;
        if (INTERNAL_TRANSFER_PATTERN.matcher(summary).find()
                && counterpartyRaw != null && counterpartyRaw.contains("陈云龙")) return true;
        return summary.contains("信用卡还款");
    }

    private String classifySummary(String summary) {
        if (summary == null || summary.isEmpty()) return "其他";
        String s = summary;
        if (s.contains("结息")) return "金融保险";
        if (s.contains("工资") || s.contains("代发")) return "工作收入";
        if (s.contains("还款")) return "金融保险";
        if (s.contains("转账存入") || s.contains("转入")) return "转账";
        if (s.contains("转账支取") || s.contains("转出")) return "转账";
        if (s.contains("消费") || s.contains("支出")) return "消费";
        if (s.contains("手续费")) return "金融保险";
        if (s.contains("跨行")) return "金融保险";
        if (s.contains("ATM") || s.contains("取现")) return "金融保险";
        if (s.contains("充值")) return "充值";
        if (s.contains("退款")) return "退款";
        return "其他";
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}

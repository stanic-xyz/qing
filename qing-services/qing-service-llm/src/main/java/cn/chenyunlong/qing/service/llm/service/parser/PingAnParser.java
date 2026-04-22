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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("PINGAN")
public class PingAnParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "PINGAN";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    // 平安银行列结构（基于Python解析器）:
    // 0:序号 1:交易日期 2:交易金额 3:余额 4:交易地点 5:摘要 6:备注 7:对手行 8:对手户名 9:对手账号
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("-?([\\d,]+\\.?\\d*)");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean dataStarted = false;
            int headerRowIdx = -1;

            // 找到表头行
            for (int i = 0; i < Math.min(15, sheet.getPhysicalNumberOfRows()); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;
                String firstCellValue = getCellValueAsString(firstCell);
                if (firstCellValue.contains("序号") || (firstCellValue.contains("No") && firstCellValue.length() < 10)) {
                    headerRowIdx = i;
                    dataStarted = true;
                    break;
                }
            }

            int startRow = headerRowIdx >= 0 ? headerRowIdx + 1 : 0;

            for (int ri = startRow; ri <= sheet.getLastRowNum(); ri++) {
                Row row = sheet.getRow(ri);
                if (row == null) continue;

                try {
                    // 列0: 序号
                    String seq = getCellValueAsString(row.getCell(0));
                    if (seq.isEmpty() || "nan".equals(seq)) continue;

                    // 列1: 交易日期
                    String dateStr = getCellValueAsString(row.getCell(1)).trim();
                    if (dateStr.isEmpty() || dateStr.contains("打印时间")) continue;

                    if (dateStr.length() == 8 || (dateStr.length() == 10 && dateStr.matches("\\d{8}"))) {
                        // yyyyMMdd 格式
                        dateStr = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8) + " 00:00:00";
                    } else if (dateStr.length() == 10 && dateStr.contains("-")) {
                        dateStr = dateStr + " 00:00:00";
                    } else if (dateStr.length() == 10 && dateStr.contains("/")) {
                        dateStr = dateStr.replace("/", "-") + " 00:00:00";
                    }

                    LocalDateTime txTime;
                    try {
                        txTime = LocalDateTime.parse(dateStr, DATE_FORMAT);
                    } catch (Exception e) {
                        try {
                            txTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        } catch (Exception e2) {
                            log.warn("无法解析日期: {}, 跳过此行", dateStr);
                            continue;
                        }
                    }

                    // 列2: 交易金额
                    BigDecimal amount = getAmountFromCell(row.getCell(2));
                    // 列3: 余额（备用）
                    BigDecimal balance = getAmountFromCell(row.getCell(3));

                    if (amount == null) {
                        // 尝试从列1解析金额
                        String amountStr = getCellValueAsString(row.getCell(1)).replace(",", "").trim();
                        Matcher m = AMOUNT_PATTERN.matcher(amountStr);
                        if (m.find()) {
                            try {
                                amount = new BigDecimal(m.group(1).replace(",", ""));
                            } catch (Exception ignored) {}
                        }
                    }

                    if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) continue;

                    // 列5: 摘要
                    String summary = getCellValueAsString(row.getCell(5)).trim();
                    // 列6: 备注
                    String notes = getCellValueAsString(row.getCell(6)).trim();
                    // 列7: 对手行
                    String cpBank = getCellValueAsString(row.getCell(7)).trim();
                    // 列8: 对手户名
                    String cpName = getCellValueAsString(row.getCell(8)).trim();
                    // 列9: 对手账号
                    String cpAccount = getCellValueAsString(row.getCell(9)).trim();

                    // 判断收支方向
                    boolean isExpense = amount.compareTo(BigDecimal.ZERO) < 0;
                    BigDecimal absAmount = amount.abs();

                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(txTime);
                    record.setAmount(absAmount);

                    if (isExpense) {
                        record.setType(TrasactionType.EXPENSE);
                    } else {
                        record.setType(TrasactionType.INCOME);
                    }

                    // 构建对手方
                    if (!cpName.isEmpty() && !"nan".equals(cpName)) {
                        Counterparty cp = new Counterparty();
                        cp.setName(cpName);
                        record.setCounterparty(cp);
                    }

                    // 描述/商户 = 摘要 + 备注
                    String description = summary;
                    if (!notes.isEmpty() && !"nan".equals(notes)) {
                        description = summary + " " + notes;
                    }
                    if (description.length() > 255) {
                        description = description.substring(0, 255);
                    }
                    record.setMerchant(description.trim());

                    // 交易状态
                    record.setStatus(mapTransactionStatus(summary, cpName));

                    // 余额
                    if (balance != null) {
                        record.setBalance(balance.abs());
                    }

                    record.setAccountName("平安银行储蓄卡(0748)");
                    record.setAccountType(AccountType.DEBIT);
                    record.setRecordRole(RecordRoleEnum.PRIMARY);
                    record.setFundSource("平安银行储蓄卡(0748)");
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析平安银行流水失败: 第 {} 行, 错误: {}", ri, e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private TransactionStatusEnum mapTransactionStatus(String summary, String cpName) {
        if (summary == null && cpName == null) return TransactionStatusEnum.SUCCESS;
        String s = (summary != null ? summary : "") + (cpName != null ? cpName : "");
        if (s.contains("成功") || s.contains("消费") || s.contains("取现") || s.contains("转账")) {
            return TransactionStatusEnum.SUCCESS;
        }
        if (s.contains("失败") || s.contains("拒绝")) {
            return TransactionStatusEnum.FAILED;
        }
        return TransactionStatusEnum.SUCCESS;
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

    private BigDecimal getAmountFromCell(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING) {
                String val = cell.getStringCellValue().replace(",", "").trim();
                if (val.isEmpty()) return null;
                return new BigDecimal(val);
            }
        } catch (Exception ignored) {}
        return null;
    }
}

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
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("CITIC_CREDIT")
public class CiticCreditParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "CITIC_CREDIT";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    // 列结构: 0=交易日期 1=入账日期 2=交易描述 3=卡末四位 4=交易币种 5=结算币种 6=交易金额 7=结算金额
    // 结算金额为负数时表示退款/还款，正数为消费

    private static final DateTimeFormatter CITIC_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    // 用于提取卡号后四位
    private static final Pattern CARD_LAST4_PATTERN = Pattern.compile("\\d{4}");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            int headerRowIdx = -1;

            // 找到表头行
            for (int i = 0; i < Math.min(10, sheet.getPhysicalNumberOfRows()); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;
                String val = getCellValueAsString(firstCell);
                if (val.contains("交易日期")) {
                    headerRowIdx = i;
                    break;
                }
            }

            int startRow = headerRowIdx >= 0 ? headerRowIdx + 1 : 0;

            for (int ri = startRow; ri <= sheet.getLastRowNum(); ri++) {
                Row row = sheet.getRow(ri);
                if (row == null) continue;

                try {
                    Cell timeCell = row.getCell(0);
                    if (timeCell == null) continue;

                    String timeStr = "";
                    if (timeCell.getCellType() == CellType.STRING) {
                        timeStr = timeCell.getStringCellValue().trim();
                    } else if (timeCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(timeCell)) {
                        timeStr = timeCell.getLocalDateTimeCellValue().format(CITIC_DATE_FORMAT);
                    }

                    if (!timeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        continue;
                    }

                    LocalDateTime txTime = LocalDateTime.parse(timeStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    // 列2: 交易描述
                    String desc = getCellValueAsString(row.getCell(2)).trim();
                    // 列3: 卡末四位
                    String cardLast4 = getCellValueAsString(row.getCell(3)).trim();

                    // 列6: 交易金额 - 根据 Python 解析器，这是实际的交易金额
                    // 正数=消费，负数=退款/还款
                    BigDecimal amount = getAmountFromCell(row.getCell(6));

                    if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                        // 尝试列7（结算金额）
                        amount = getAmountFromCell(row.getCell(7));
                    }

                    if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) continue;

                    boolean isRefund = amount.compareTo(BigDecimal.ZERO) < 0;
                    BigDecimal absAmount = amount.abs();

                    // 判断收支类型
                    TrasactionType txType;
                    if (isRefund) {
                        txType = TrasactionType.INCOME; // 退款/还款
                    } else if (desc.contains("还款") || desc.contains("存入") || desc.contains("代付")) {
                        txType = TrasactionType.INCOME;
                    } else {
                        txType = TrasactionType.EXPENSE;
                    }

                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(txTime);
                    record.setAmount(absAmount);
                    record.setType(txType);

                    // 构建对手方（从描述中提取）
                    String cpName = extractCounterparty(desc);
                    if (!cpName.isEmpty()) {
                        Counterparty cp = new Counterparty();
                        cp.setName(cpName);
                        record.setCounterparty(cp);
                    }

                    // 商家/描述
                    String merchant = cleanMerchant(desc);
                    record.setMerchant(merchant);

                    // 分类
                    record.setSubCategory(mapCategory(desc));

                    record.setStatus(TransactionStatusEnum.SUCCESS);
                    record.setAccountName("中信银行信用卡(" + cardLast4 + ")");
                    record.setAccountType(AccountType.CREDIT);
                    record.setRecordRole(RecordRoleEnum.TRACE);
                    record.setFundSource("中信银行信用卡(" + cardLast4 + ")");
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    // 原始ID
                    record.setOriginalId("citic_" + timeStr.replace("-", "") + "_" + cardLast4);

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析中信信用卡流水失败: 第 {} 行, 错误: {}", ri, e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private String extractCounterparty(String desc) {
        if (desc == null || desc.isEmpty()) return "";
        // 中信描述格式如: "财付通－重庆市以纯服装店" 或 "银联－上海拉扎斯信息科技"
        // 提取 "-" 后的部分作为对手方
        int dashIdx = desc.indexOf("－");
        if (dashIdx >= 0 && dashIdx < desc.length() - 1) {
            return desc.substring(dashIdx + 1).trim();
        }
        dashIdx = desc.indexOf("-");
        if (dashIdx >= 0 && dashIdx < desc.length() - 1) {
            return desc.substring(dashIdx + 1).trim();
        }
        return "";
    }

    private String cleanMerchant(String desc) {
        if (desc == null) return "";
        // 移除卡号相关信息
        String cleaned = desc.replaceAll("\\d{16,19}", "");
        // 移除 CHN 币种标注
        cleaned = cleaned.replace("CHN", "").replace("USD", "").trim();
        if (cleaned.length() > 255) {
            cleaned = cleaned.substring(0, 255);
        }
        return cleaned;
    }

    private String mapCategory(String desc) {
        if (desc == null || desc.isEmpty()) return "消费";
        String lower = desc.toLowerCase();
        if (lower.contains("还款") || lower.contains("存入")) return "信用卡还款";
        if (lower.contains("骑行") || lower.contains("美团")) return "交通出行";
        if (lower.contains("加油") || lower.contains("中石油") || lower.contains("中石化")) return "交通出行";
        if (lower.contains("餐饮") || lower.contains("外卖") || lower.contains("饿了么") || lower.contains("美团")) return "餐饮饮食";
        if (lower.contains("云闪付")) return "消费";
        if (lower.contains("电商") || lower.contains("京东") || lower.contains("淘宝") || lower.contains("拼多多")) return "购物消费";
        if (lower.contains("电影") || lower.contains("视频") || lower.contains("会员")) return "娱乐休闲";
        if (lower.contains("保险")) return "金融保险";
        return "消费";
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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

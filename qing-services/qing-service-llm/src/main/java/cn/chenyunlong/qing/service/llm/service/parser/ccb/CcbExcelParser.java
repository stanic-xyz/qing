package cn.chenyunlong.qing.service.llm.service.parser.ccb;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.service.parser.BaseFileParser;
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

/**
 * 建设银行Excel账单解析器
 * <p>
 * 表格结构:
 * 序号	摘要	币别	钞汇	交易日期	交易金额	账户余额	交易地点/附言	对方账号与户名
 * 1	转账存入	人民币元	钞	20150904	20.00	20.00	四川省分行营管部	6217003810043311771/张婷
 */
@Slf4j
@Component("CCB_EXCEL")
public class CcbExcelParser extends BaseFileParser {

    private static final String CHANNEL_CODE = "CCB";

    private static final DateTimeFormatter CCB_EXCEL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter CCB_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMdd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    @Override
    public List<String> supportedFileExtensions() {
        return List.of("xls", "xlsx");
    }

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            int currentRow = 0;

            for (Row row : sheet) {
                // 跳过前4行（通常是表头和说明信息）
                if (currentRow++ < 4) {
                    continue;
                }

                try {
                    TransactionRecord record = parseRow(row);
                    if (record != null) {
                        records.add(record);
                    }
                } catch (Exception e) {
                    log.warn("解析建设银行Excel流水失败，行号: {}", row.getRowNum(), e);
                }
            }
        }

        return wrapResult(records);
    }

    /**
     * 解析单行数据
     * <p>
     * 列索引:
     * 0: 序号
     * 1: 摘要
     * 2: 币别
     * 3: 钞汇
     * 4: 交易日期
     * 5: 交易金额
     * 6: 账户余额
     * 7: 交易地点/附言
     * 8: 对方账号与户名
     */
    private TransactionRecord parseRow(Row row) {
        // 从第4列读取交易日期
        Cell dateCell = row.getCell(4);
        if (dateCell == null) {
            return null;
        }

        String dateStr = extractCellStringValue(dateCell);
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        // 支持两种日期格式：20150904 或 2015-09-04 10:30:00
        LocalDateTime transactionTime;
        if (dateStr.matches("\\d{8}")) {
            // 纯数字日期格式：20150904
            transactionTime = LocalDateTime.parse(dateStr, CCB_DATE_FORMAT);
        } else if (dateStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            // 完整日期时间格式
            transactionTime = LocalDateTime.parse(dateStr, CCB_EXCEL_FORMAT);
        } else {
            return null;
        }

        TransactionRecord record = new TransactionRecord();
        record.setTransactionTime(transactionTime);

        // 从第1列读取摘要（交易类型描述）
        Cell summaryCell = row.getCell(1);
        String summary = summaryCell != null ? summaryCell.getStringCellValue().trim() : "";

        // 从第5列读取交易金额
        Cell amountCell = row.getCell(5);
        if (amountCell != null) {
            String amountStr = extractCellStringValue(amountCell);
            if (amountStr != null && !amountStr.isEmpty()) {
                BigDecimal amount = new BigDecimal(amountStr.replace(",", ""));
                record.setAmount(amount.abs());

                // 根据金额正负判断交易类型
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    record.setType(TrasactionType.EXPENSE);
                } else {
                    record.setType(TrasactionType.INCOME);
                }
            }
        }

        // 从第6列读取账户余额
        Cell balanceCell = row.getCell(6);
        if (balanceCell != null) {
            String balanceStr = extractCellStringValue(balanceCell);
            if (balanceStr != null && !balanceStr.isEmpty()) {
                record.setBalance(new BigDecimal(balanceStr.replace(",", "")));
            }
        }

        // 从第7列读取交易地点/附言(渠道）
        Cell locationCell = row.getCell(7);

        StringBuilder remarkBuilder = new StringBuilder();
        if (locationCell != null) {
            String location = locationCell.getStringCellValue().trim();
            if (!location.isEmpty()) {
                record.setMerchant(location);
                remarkBuilder.append(location);
            } else {
                remarkBuilder.append(" ");
            }
        }

        // 从第8列读取对方账号与户名
        Cell counterpartyCell = row.getCell(8);
        if (counterpartyCell != null) {
            String counterparty = counterpartyCell.getStringCellValue().trim();
            if (!counterparty.isEmpty()) {
                // 格式可能是: 6217003810043311771/张婷
                if (counterparty.contains("/")) {
                    String[] parts = counterparty.split("/", 2);
                    record.setRemark(parts.length > 1 ? parts[1] : counterparty);
                } else {
                    record.setRemark(counterparty);
                }
            }
        }

        // 设置默认值
        record.setAccountName("建设银行");
        record.setAccountType(AccountType.DEBIT);
        record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
        record.setConfirmed(false);
        record.setStatus(TransactionStatusEnum.SUCCESS);

        return record;
    }

    /**
     * 提取单元格值为字符串
     */
    private String extractCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().format(CCB_EXCEL_FORMAT);
                } else {
                    // 对于数字类型，保留原始精度
                    BigDecimal value = BigDecimal.valueOf(cell.getNumericCellValue());
                    return value.toPlainString();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            default:
                return null;
        }
    }
}

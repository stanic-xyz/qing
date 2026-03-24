package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("PINGAN")
public class PingAnParser extends BaseFileParser {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean dataStarted = false;
            
            for (Row row : sheet) {
                if (row == null) continue;
                
                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;
                
                String firstCellValue = getCellValueAsString(firstCell);
                
                // 表头判断，平安银行的表头可能包含"交易日期"
                if (!dataStarted) {
                    if (firstCellValue.contains("交易日期") || firstCellValue.contains("时间")) {
                        dataStarted = true;
                    }
                    continue;
                }
                
                // 可能是结尾说明
                if (firstCellValue.isEmpty() || firstCellValue.contains("打印时间") || firstCellValue.contains("备注")) {
                    continue;
                }
                
                try {
                    // 假设列顺序：0:交易日期/时间, 1:收支标志/类型, 2:交易金额, 3:账户余额, 4:对方账号, 5:对方户名, 6:摘要/交易类型
                    // 需要根据真实账单灵活调整，这里提供一个基础映射
                    String timeStr = firstCellValue.trim();
                    if (timeStr.length() == 10) {
                        timeStr += " 00:00:00"; // 只有日期没有时间
                    }
                    
                    TransactionRecord record = new TransactionRecord();
                    record.setChannel("PINGAN");
                    try {
                        record.setTransactionTime(LocalDateTime.parse(timeStr, DATE_FORMAT));
                    } catch (Exception e) {
                        // 尝试其他格式或跳过
                        continue;
                    }
                    
                    // 金额可能在特定列，这里需要实际情况调整，假设在2列和3列(支出/存入)
                    BigDecimal expense = getAmountFromCell(row.getCell(2)); // 假设第2列是支出
                    BigDecimal income = getAmountFromCell(row.getCell(3));  // 假设第3列是存入
                    
                    if (expense != null && expense.compareTo(BigDecimal.ZERO) > 0) {
                        record.setAmount(expense);
                        record.setType("EXPENSE");
                    } else if (income != null && income.compareTo(BigDecimal.ZERO) > 0) {
                        record.setAmount(income);
                        record.setType("INCOME");
                    } else {
                        // 如果在同一列
                        BigDecimal amount = getAmountFromCell(row.getCell(1));
                        if (amount != null) {
                            record.setAmount(amount.abs());
                            record.setType(amount.compareTo(BigDecimal.ZERO) >= 0 ? "INCOME" : "EXPENSE");
                        } else {
                            continue;
                        }
                    }
                    
                    // 对方信息
                    record.setCounterparty(getCellValueAsString(row.getCell(5))); // 假设对方户名在第5列
                    record.setMerchant(getCellValueAsString(row.getCell(6))); // 假设摘要在第6列
                    
                    record.setAccountName("平安银行");
                    record.setAccountType("DEBIT_CARD");
                    record.setStatus("SUCCESS");
                    record.setReconciliationStatus("PENDING");
                    record.setConfirmed(false);
                    
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析平安银行流水失败: 第 {} 行, 错误: {}", row.getRowNum(), e.getMessage());
                }
            }
        }
        return records;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
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
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
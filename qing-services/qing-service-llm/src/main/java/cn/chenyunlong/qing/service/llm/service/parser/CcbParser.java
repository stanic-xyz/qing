package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

@Slf4j
@Component("CCB")
public class CcbParser extends BaseFileParser {

    private static final DateTimeFormatter CCB_EXCEL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter CCB_PDF_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMdd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private static final Pattern TXT_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s+(.+)$");
    // 匹配如: 1 转账存入 20150904 20.00 20.00 四川省分行营管部 6217003810043311771/张婷
    // 或: 14 代扣学杂 20150907 -7,050.00 154.00 代扣学杂 10151088533622959900801222/成都理
    private static final Pattern PDF_LINE_PATTERN = Pattern.compile("^\\d+\\s+(.+?)\\s+(\\d{8})\\s+([\\-\\d,.]+)\\s+([\\d,.]+)\\s*(.*)$");

    @Override
    public List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        
        if (originalFilename.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");
                
                TransactionRecord currentRecord = null;
                StringBuilder remarkBuilder = new StringBuilder();

                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("中国建设银行") || line.startsWith("卡号/账号") || line.startsWith("当前时间段") || line.startsWith("序号") || line.startsWith("生成时间") || line.startsWith("温馨提示") || line.startsWith("- 第") || line.contains("总支出")) {
                        continue;
                    }

                    Matcher matcher = PDF_LINE_PATTERN.matcher(line);
                    if (matcher.find()) {
                        if (currentRecord != null) {
                            finalizeRecord(currentRecord, remarkBuilder.toString());
                            records.add(currentRecord);
                        }

                        currentRecord = new TransactionRecord();
                        currentRecord.setChannel("CCB");
                        currentRecord.setCategory(matcher.group(1).trim());
                        currentRecord.setTransactionTime(LocalDateTime.parse(matcher.group(2), CCB_PDF_FORMAT));

                        String amountStr = matcher.group(3).replace(",", "");
                        BigDecimal amount = new BigDecimal(amountStr);
                        if (amount.compareTo(BigDecimal.ZERO) < 0) {
                            currentRecord.setType("EXPENSE");
                            currentRecord.setAmount(amount.abs());
                        } else {
                            currentRecord.setType("INCOME");
                            currentRecord.setAmount(amount);
                        }

                        currentRecord.setBalance(new BigDecimal(matcher.group(4).replace(",", "")));
                        
                        currentRecord.setAccountName("建设银行");
                        currentRecord.setAccountType("DEBIT");
                        currentRecord.setReconciliationStatus("PENDING");
                        currentRecord.setConfirmed(false);
                        currentRecord.setStatus("SUCCESS");

                        remarkBuilder.setLength(0);
                        String restInfo = matcher.group(5).trim();
                        if (!restInfo.isEmpty()) {
                            remarkBuilder.append(restInfo);
                        }
                    } else if (currentRecord != null) {
                        // 将不能匹配为新记录的行，追加到当前记录的备注/对方信息中
                        remarkBuilder.append(line);
                    }
                }
                
                if (currentRecord != null) {
                    finalizeRecord(currentRecord, remarkBuilder.toString());
                    records.add(currentRecord);
                }
            }
        } else if (originalFilename.toLowerCase().endsWith(".xls") || originalFilename.toLowerCase().endsWith(".xlsx")) {
            try (Workbook workbook = WorkbookFactory.create(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    try {
                        Cell timeCell = row.getCell(0);
                        if (timeCell == null) continue;
                        String timeStr = timeCell.getStringCellValue().trim();
                        if (!timeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) continue;
                        
                        TransactionRecord record = new TransactionRecord();
                        record.setChannel("CCB");
                        record.setTransactionTime(LocalDateTime.parse(timeStr, CCB_EXCEL_FORMAT));
                        // 根据建设银行表格格式提取字段
                        Cell amountCell = row.getCell(2);
                        if (amountCell != null) {
                            record.setAmount(new BigDecimal(amountCell.getStringCellValue().replace(",", "")));
                        }
                        record.setAccountName("建设银行");
                        record.setAccountType("DEBIT");
                        record.setReconciliationStatus("PENDING");
                        record.setConfirmed(false);
                        record.setStatus("SUCCESS");
                        record.setType("EXPENSE"); // 需要根据具体列判断
                        records.add(record);
                    } catch (Exception e) {
                        log.warn("解析建设银行Excel流水失败");
                    }
                }
            }
        } else {
            String content = new String(inputStream.readAllBytes(), "GBK");
            String[] lines = content.split("\\r?\\n");
            for (String line : lines) {
                parseLine(line, records);
            }
        }
        return records;
    }

    private void finalizeRecord(TransactionRecord record, String fullRemark) {
        String remark = fullRemark.trim();
        if (remark.length() > 255) {
            remark = remark.substring(0, 255);
        }
        // 尝试从附言/对方信息中提取对方户名或地点
        String[] parts = remark.split(" ");
        if (parts.length >= 2) {
            record.setMerchant(parts[0]);
            record.setCounterparty(parts[1]);
            if (parts.length > 2) {
                record.setRemark(remark.substring(parts[0].length() + parts[1].length() + 2));
            }
        } else {
            record.setCounterparty(remark);
            record.setMerchant(remark);
        }
    }

    private void parseLine(String line, List<TransactionRecord> records) {
        line = line.trim();
        Matcher matcher = TXT_PATTERN.matcher(line);
        if (matcher.find()) {
            try {
                String timeStr = matcher.group(1);
                String restStr = matcher.group(2);
                
                String[] parts = restStr.split("\\s+");
                if (parts.length < 3) return;

                TransactionRecord record = new TransactionRecord();
                record.setChannel("CCB");
                record.setTransactionTime(LocalDateTime.parse(timeStr, CCB_EXCEL_FORMAT));
                
                record.setAmount(new BigDecimal(parts[parts.length - 2].replace(",", "")));
                record.setCounterparty(parts[0]);
                record.setMerchant(parts[1]);
                
                record.setAccountName("建设银行");
                record.setAccountType("DEBIT");
                record.setReconciliationStatus("PENDING");
                record.setConfirmed(false);
                record.setStatus("SUCCESS");
                record.setType("EXPENSE");
                
                records.add(record);
            } catch (Exception e) {
                log.warn("解析建设银行流水失败: {}", line);
            }
        }
    }
}
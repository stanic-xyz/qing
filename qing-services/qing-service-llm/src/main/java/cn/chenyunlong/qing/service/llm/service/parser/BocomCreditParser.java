package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("BOCOM_CREDIT")
public class BocomCreditParser extends BaseFileParser {

    // 匹配类似 "2023/10/01" 或 "2023-10-01" 或 "10/01" 的日期，加上金额等
    private static final Pattern TRANSACTION_PATTERN = Pattern.compile("^(\\d{4}[-/]\\d{2}[-/]\\d{2}|\\d{2}[-/]\\d{2})\\s+(.*?)(\\s+-?\\d+\\.\\d{2})$");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        
        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(document);
            
            String[] lines = text.split("\\r?\\n");
            String currentYear = String.valueOf(LocalDate.now().getYear());
            
            for (String line : lines) {
                line = line.trim();
                
                // 尝试提取年份
                if (line.contains("年") && line.contains("月") && line.contains("账单")) {
                    Matcher m = Pattern.compile("(\\d{4})年").matcher(line);
                    if (m.find()) {
                        currentYear = m.group(1);
                    }
                }
                
                // 简单正则匹配交易行
                Matcher matcher = TRANSACTION_PATTERN.matcher(line);
                if (matcher.find()) {
                    try {
                        String dateStr = matcher.group(1);
                        String description = matcher.group(2).trim();
                        String amountStr = matcher.group(3).trim();
                        
                        TransactionRecord record = new TransactionRecord();
                        record.setChannel("BOCOM_CREDIT");
                        
                        // 补充年份
                        if (!dateStr.contains(currentYear) && dateStr.length() <= 5) {
                            dateStr = currentYear + "-" + dateStr.replace("/", "-");
                        } else {
                            dateStr = dateStr.replace("/", "-");
                        }
                        
                        try {
                            if (dateStr.length() == 10) {
                                record.setTransactionTime(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
                            }
                        } catch (Exception e) {
                            continue;
                        }
                        
                        BigDecimal amount = new BigDecimal(amountStr);
                        if (amount.compareTo(BigDecimal.ZERO) < 0) {
                            record.setType("INCOME"); // 信用卡负数通常表示还款/退款
                            record.setAmount(amount.abs());
                        } else {
                            record.setType("EXPENSE");
                            record.setAmount(amount);
                        }
                        
                        record.setMerchant(description);
                        record.setAccountName("交通银行信用卡");
                        record.setAccountType("CREDIT_CARD");
                        record.setReconciliationStatus("PENDING");
                        record.setConfirmed(false);
                        record.setStatus("SUCCESS");
                        
                        records.add(record);
                    } catch (Exception e) {
                        log.warn("解析交通银行信用卡流水失败: {}", line);
                    }
                }
            }
        }
        return wrapResult(records);
    }
}
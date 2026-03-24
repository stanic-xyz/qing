package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("CMB")
public class CmbParser extends BaseFileParser {

    private static final DateTimeFormatter CMB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 匹配如: 2019-12-06 CNY 1,000.00 1,000.00 网联收款 或者 -1,000.00
    private static final Pattern PDF_DATE_LINE_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s+(\\w+)\\s+([\\-\\d,.]+)\\s+([\\d,.]+)\\s+(.+)$");

    @Override
    public List<TransactionRecord> parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        
        if (originalFilename.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");
                
                TransactionRecord currentRecord = null;
                StringBuilder merchantBuilder = new StringBuilder();
                StringBuilder remarkBuilder = new StringBuilder();
                
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("记账日期") || line.startsWith("Date Currency") || line.startsWith("Transaction") || line.startsWith("Amount") || line.startsWith("Balance") || line.matches("^\\d+/\\d+$")) {
                        continue;
                    }

                    Matcher matcher = PDF_DATE_LINE_PATTERN.matcher(line);
                    if (matcher.find()) {
                        // 遇到新的记录，先保存上一条
                        if (currentRecord != null) {
                            finalizeRecord(currentRecord, merchantBuilder.toString(), remarkBuilder.toString());
                            records.add(currentRecord);
                        }
                        
                        currentRecord = new TransactionRecord();
                        currentRecord.setChannel("CMB");
                        String timeStr = matcher.group(1) + " 00:00:00";
                        currentRecord.setTransactionTime(LocalDateTime.parse(timeStr, CMB_FORMAT));
                        
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
                        currentRecord.setCategory(matcher.group(5).trim()); // 交易摘要作为分类
                        
                        currentRecord.setAccountName("招商银行");
                        currentRecord.setAccountType("DEBIT");
                        currentRecord.setReconciliationStatus("PENDING");
                        currentRecord.setConfirmed(false);
                        currentRecord.setStatus("SUCCESS");
                        
                        merchantBuilder.setLength(0);
                        remarkBuilder.setLength(0);
                    } else if (currentRecord != null) {
                        // 附加信息行（可能是对手信息、备注等）
                        // 招商银行PDF的结构通常第一行是交易摘要，随后几行可能包含对手账号、名称和客户摘要。
                        // 简单处理：将所有的后续行拼接作为商户/对方信息/备注
                        if (line.contains("支付宝") || line.contains("财付通") || line.contains("网联")) {
                            merchantBuilder.append(line).append(" ");
                        } else if (line.matches(".*\\d{10,}.*")) {
                             // 类似账号信息
                             remarkBuilder.append(line).append(" ");
                        } else {
                            merchantBuilder.append(line).append(" ");
                        }
                    }
                }
                
                // 添加最后一条记录
                if (currentRecord != null) {
                    finalizeRecord(currentRecord, merchantBuilder.toString(), remarkBuilder.toString());
                    records.add(currentRecord);
                }
            }
        } else {
            // TXT 解析
            String content = new String(inputStream.readAllBytes(), "GBK"); // 招行TXT大多是GBK
            String[] lines = content.split("\\r?\\n");
            for (String line : lines) {
                // 暂时保留TXT处理逻辑
            }
        }
        return records;
    }

    private void finalizeRecord(TransactionRecord record, String merchantStr, String remarkStr) {
        String combinedMerchant = merchantStr.trim();
        if (combinedMerchant.length() > 255) {
            combinedMerchant = combinedMerchant.substring(0, 255);
        }
        record.setMerchant(combinedMerchant);
        record.setCounterparty(combinedMerchant); // 简化处理
        
        String combinedRemark = remarkStr.trim();
        if (combinedRemark.length() > 255) {
            combinedRemark = combinedRemark.substring(0, 255);
        }
        record.setRemark(combinedRemark);
    }
}

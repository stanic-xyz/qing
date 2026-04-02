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
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("BOC_CREDIT")
public class BocCreditParser extends BaseFileParser {

    private static final DateTimeFormatter BOC_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 匹配如: 07/03 07/04 2688 (可能后面跟有部分描述)
    private static final Pattern RECORD_START_PATTERN = Pattern.compile("^(\\d{2}/\\d{2})\\s+(\\d{2}/\\d{2})\\s+(\\d{4})(.*)$");
    // 匹配标题中的年份如：中国银行信用卡账单(2023年08月)
    private static final Pattern YEAR_PATTERN = Pattern.compile(".*\\((\\d{4})年.*\\).*");
    // 匹配末尾的金额
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^(.*?)\\s*([\\d,]+\\.\\d{2})$");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        
        if (originalFilename.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");
                
                String year = "2023"; // 默认回退值
                TransactionRecord currentRecord = null;
                StringBuilder descBuilder = new StringBuilder();
                boolean isTransactionSection = false;

                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("第") || line.endsWith("页") || line.startsWith("积分奖励计划")) {
                        if (line.startsWith("积分奖励计划")) {
                            isTransactionSection = false; // 结束交易明细部分
                        }
                        continue;
                    }
                    
                    Matcher yearMatcher = YEAR_PATTERN.matcher(line);
                    if (yearMatcher.matches()) {
                        year = yearMatcher.group(1);
                        continue;
                    }
                    
                    if (line.contains("交易日") || line.contains("Transaction Date")) {
                        isTransactionSection = true;
                        continue;
                    }
                    
                    if (!isTransactionSection) {
                        continue;
                    }

                    Matcher startMatcher = RECORD_START_PATTERN.matcher(line);
                    if (startMatcher.find()) {
                        // 结算上一条记录
                        if (currentRecord != null) {
                            finalizeRecord(currentRecord, descBuilder.toString());
                            records.add(currentRecord);
                        }
                        
                        currentRecord = new TransactionRecord();
                        currentRecord.setChannel("BOC_CREDIT");
                        
                        // startMatcher.group(1) 是交易日如 "07/03"
                        String dateStr = startMatcher.group(1).replace("/", "-");
                        String timeStr = year + "-" + dateStr + " 00:00:00";
                        currentRecord.setTransactionTime(LocalDateTime.parse(timeStr, BOC_DATE_FORMAT));
                        
                        currentRecord.setAccountName("中国银行信用卡");
                        currentRecord.setAccountType("CREDIT");
                        currentRecord.setReconciliationStatus("PENDING");
                        currentRecord.setConfirmed(false);
                        currentRecord.setStatus("SUCCESS");
                        
                        descBuilder.setLength(0);
                        String restOfLine = startMatcher.group(4).trim();
                        if (!restOfLine.isEmpty()) {
                            descBuilder.append(restOfLine);
                        }
                    } else if (currentRecord != null) {
                        // 属于当前记录的描述或金额行
                        if (!line.matches(".*\\d{4}-\\d{2}-\\d{2}.*") && !line.startsWith("欠款") && !line.contains("存款/欠款余额")) {
                            if (descBuilder.length() > 0) {
                                descBuilder.append(" ");
                            }
                            descBuilder.append(line);
                        }
                    }
                }
                
                // 结算最后一条记录
                if (currentRecord != null) {
                    finalizeRecord(currentRecord, descBuilder.toString());
                    records.add(currentRecord);
                }
            }
        }
        return wrapResult(records);
    }

    private void finalizeRecord(TransactionRecord record, String fullDesc) {
        // 去除可能多余的货币符号如 CHN
        String cleanedDesc = fullDesc.replace("CHN", "").trim();
        Matcher amountMatcher = AMOUNT_PATTERN.matcher(cleanedDesc);
        
        String desc = cleanedDesc;
        if (amountMatcher.matches()) {
            desc = amountMatcher.group(1).trim();
            String amountStr = amountMatcher.group(2).replace(",", "");
            record.setAmount(new BigDecimal(amountStr));
        } else {
            // 解析不到金额则跳过或置0
            record.setAmount(BigDecimal.ZERO);
        }
        
        if (desc.length() > 255) {
            desc = desc.substring(0, 255);
        }
        record.setMerchant(desc);
        record.setCounterparty(desc); // 信用卡通常没有独立对方列
        
        if (desc.contains("还款") || desc.contains("代付") || desc.contains("存入")) {
            record.setType("INCOME");
        } else {
            record.setType("EXPENSE");
        }
    }
}
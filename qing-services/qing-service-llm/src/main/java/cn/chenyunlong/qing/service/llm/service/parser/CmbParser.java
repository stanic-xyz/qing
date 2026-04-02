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
@Component("CMB")
public class CmbParser extends BaseFileParser {

    private static final DateTimeFormatter CMB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 匹配如: 2019-12-06 CNY 1,000.00 1,000.00 网联收款 或者 -1,000.00
    private static final Pattern PDF_DATE_LINE_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s+(\\w+)\\s+([\\-\\d,.]+)\\s+([\\d,.]+)\\s+(.+)$");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
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
                            finalizeRecord(currentRecord, merchantBuilder.toString());
                            records.add(currentRecord);
                        }

                        merchantBuilder.setLength(0);
                        remarkBuilder.setLength(0);

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

                        // group(5) 可能是 "网联收款" 也可能连带了其他信息
                        String rest = matcher.group(5).trim();
                        String[] parts = rest.split("\\s+", 2);
                        currentRecord.setCategory(parts[0]); // 第一部分作为交易摘要
                        if (parts.length > 1) {
                            merchantBuilder.append(parts[1]).append(" ");
                        }
                    } else if (currentRecord != null) {
                        // 附加信息行（包含对手账号、名称和客户摘要）
                        // 统一收集到 merchantBuilder 中，避免遗漏
                        merchantBuilder.append(line).append(" ");
                    }
                }

                // 添加最后一条记录
                if (currentRecord != null) {
                    finalizeRecord(currentRecord, merchantBuilder.toString());
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
        return wrapResult(records);
    }

    private void finalizeRecord(TransactionRecord record, String extraInfo) {
        String combined = extraInfo.replaceAll("\\s+", " ").trim();
        if (combined.length() > 255) {
            combined = combined.substring(0, 255);
        }
        // 将所有后续信息全部作为对手信息和备注，防止任何数据丢失
        record.setCounterparty(combined);
        record.setRemark(combined);

        // 商户字段初始置空，交由 Matcher 智能提取，提取不到则前端显示未提取
        record.setMerchant(null);
    }
}

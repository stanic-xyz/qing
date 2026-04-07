package cn.chenyunlong.qing.service.llm.service.parser.ccb;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.service.parser.BaseFileParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

/**
 * 建设银行PDF账单解析器
 * <p>
 * PDF格式示例:
 * 1 转账存入 20150904 20.00 20.00 四川省分行营管部 6217003810043311771/张婷
 * 14 代扣学杂 20150907 -7,050.00 154.00 代扣学杂 10151088533622959900801222/成都理工大学学费代收户
 */
@Slf4j
@Component("CCB_PDF")
public class CcbPdfParser extends BaseFileParser {

    private static final String CHANNEL_CODE = "CCB";

    private static final DateTimeFormatter CCB_PDF_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMdd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    // 匹配如: 1 转账存入 20150904 20.00 20.00 四川省分行营管部 6217003810043311771/张婷
    // 或: 14 代扣学杂 20150907 -7,050.00 154.00 代扣学杂 10151088533622959900801222/成都理
    private static final Pattern PDF_LINE_PATTERN = Pattern.compile(
            "^\\d+\\s+(.+?)\\s+(\\d{8})\\s+([\\-\\d,.]+)\\s+([\\d,.]+)\\s*(.*)$"
    );

    @Override
    public List<String> supportedFileExtensions() {
        return List.of("pdf");
    }

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            TransactionRecord currentRecord = null;
            StringBuilder remarkBuilder = new StringBuilder();

            for (String line : lines) {
                line = line.trim();

                // 跳过表头、页脚和无关信息
                if (shouldSkipLine(line)) {
                    continue;
                }

                Matcher matcher = PDF_LINE_PATTERN.matcher(line);
                if (matcher.find()) {
                    // 遇到新的记录，先保存上一条
                    if (currentRecord != null) {
                        finalizeRecord(currentRecord, remarkBuilder.toString());
                        records.add(currentRecord);
                    }

                    // 创建新记录
                    currentRecord = createTransactionRecord(matcher);

                    // 重置备注收集器
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

            // 添加最后一条记录
            if (currentRecord != null) {
                finalizeRecord(currentRecord, remarkBuilder.toString());
                records.add(currentRecord);
            }
        }

        return wrapResult(records);
    }

    /**
     * 判断是否应该跳过该行
     */
    private boolean shouldSkipLine(String line) {
        if (line.isEmpty()) {
            return true;
        }

        // 跳过表头、页脚和统计信息
        return line.startsWith("中国建设银行") ||
                line.startsWith("卡号/账号") ||
                line.startsWith("当前时间段") ||
                line.startsWith("序号") ||
                line.startsWith("生成时间") ||
                line.startsWith("温馨提示") ||
                line.startsWith("- 第") ||
                line.contains("总支出") ||
                line.contains("总收入");
    }

    /**
     * 从正则匹配结果创建交易记录
     */
    private TransactionRecord createTransactionRecord(Matcher matcher) {
        TransactionRecord record = new TransactionRecord();

        // 解析交易时间
        String dateStr = matcher.group(2);
        record.setTransactionTime(LocalDateTime.parse(dateStr, CCB_PDF_FORMAT));

        // 解析交易金额
        String amountStr = matcher.group(3).replace(",", "");
        BigDecimal amount = new BigDecimal(amountStr);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            record.setType(TrasactionType.EXPENSE);
            record.setAmount(amount.abs());
        } else {
            record.setType(TrasactionType.INCOME);
            record.setAmount(amount);
        }

        // 解析账户余额
        record.setBalance(new BigDecimal(matcher.group(4).replace(",", "")));

        // 设置默认值
        record.setAccountName("建设银行");
        record.setAccountType(AccountType.DEBIT);
        record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
        record.setConfirmed(false);
        record.setStatus(TransactionStatusEnum.SUCCESS);

        return record;
    }

    /**
     * 完成记录信息，提取商户和备注
     */
    private void finalizeRecord(TransactionRecord record, String fullRemark) {
        String remark = fullRemark.trim();
        if (remark.length() > 255) {
            remark = remark.substring(0, 255);
        }

        // 尝试从附言/对方信息中提取对方户名或地点
        String[] parts = remark.split(" ");
        if (parts.length >= 2) {
            record.setMerchant(parts[0]);
            if (parts.length > 2) {
                record.setRemark(remark.substring(parts[0].length() + parts[1].length() + 2));
            }
        } else {
            record.setMerchant(remark);
        }
    }
}

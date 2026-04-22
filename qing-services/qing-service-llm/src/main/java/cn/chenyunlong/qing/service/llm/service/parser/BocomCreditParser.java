package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("BOCOM_CREDIT")
public class BocomCreditParser extends BaseFileParser {

    private static final String CHANNEL_CODE = "BOCOM_CREDIT";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    // 交通银行格式:
    // 2026/02/20 2026/02/20 （特约）电费缴纳消费 ¥ 100.00 ¥ 100.00
    // ¥ 负数 = 还款/退款（收入），¥ 正数 = 消费（支出）
    private static final Pattern TRANSACTION_PATTERN = Pattern.compile(
            "^(\\d{4}/\\d{2}/\\d{2})\\s+(\\d{4}/\\d{2}/\\d{2})\\s+(.+?)(?:¥\\s*)?(-?[\\d,]+\\.?\\d*)\\s*$",
            Pattern.DOTALL
    );

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(document);

            String[] lines = text.split("\\r?\\n");
            String currentYear = String.valueOf(LocalDate.now().getYear());
            String cardLast4 = "7581"; // 默认值，从文件名或内容中提取

            // 从文件名提取卡号末四位
            if (originalFilename != null) {
                Matcher cardMatcher = Pattern.compile("尾号(\\d{4})").matcher(originalFilename);
                if (cardMatcher.find()) {
                    cardLast4 = cardMatcher.group(1);
                }
            }

            // 从内容提取年份
            for (String line : lines) {
                if (line.contains("年") && line.contains("月") && line.contains("账单")) {
                    Matcher m = Pattern.compile("(\\d{4})年").matcher(line);
                    if (m.find()) {
                        currentYear = m.group(1);
                    }
                    Matcher cardM = Pattern.compile("尾号(\\d{4})").matcher(line);
                    if (cardM.find()) {
                        cardLast4 = cardM.group(1);
                    }
                }
            }

            String pendingLine = "";

            for (int li = 0; li < lines.length; li++) {
                String rawLine = lines[li];
                String line = rawLine.trim();

                if (line.isEmpty()) continue;

                // 跳过标题和说明行
                if (shouldSkip(line)) continue;

                // 尝试匹配交易行
                Matcher matcher = TRANSACTION_PATTERN.matcher(line);
                if (matcher.find()) {
                    String dateStr = matcher.group(1).replace("/", "-");
                    String description = matcher.group(3).trim();
                    String amountStr = matcher.group(4).replace(",", "").trim();

                    // 补充年份
                    if (dateStr.length() == 6) { // MM-DD 格式
                        dateStr = currentYear + "-" + dateStr;
                    }

                    // 清理描述中的卡号信息
                    description = cleanDescription(description);

                    try {
                        BigDecimal amount = new BigDecimal(amountStr);
                        TransactionRecord record = new TransactionRecord();

                        LocalDateTime txTime;
                        if (dateStr.length() == 10) {
                            txTime = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                        } else {
                            continue;
                        }

                        record.setTransactionTime(txTime);

                        // 交通银行：正数=支出（消费），负数=收入（还款/退款）
                        if (amount.compareTo(BigDecimal.ZERO) < 0) {
                            record.setType(TrasactionType.INCOME);
                            record.setAmount(amount.abs());
                        } else {
                            record.setType(TrasactionType.EXPENSE);
                            record.setAmount(amount);
                        }

                        record.setMerchant(description);
                        record.setSubCategory(mapCategory(description));
                        record.setStatus(TransactionStatusEnum.SUCCESS);
                        record.setAccountName("交通银行信用卡(" + cardLast4 + ")");
                        record.setAccountType(AccountType.CREDIT);
                        record.setRecordRole(RecordRoleEnum.TRACE);
                        record.setFundSource("交通银行信用卡(" + cardLast4 + ")");
                        record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                        record.setConfirmed(false);
                        record.setOriginalId("bocom_" + dateStr.replace("-", "") + "_" + cardLast4);

                        records.add(record);
                    } catch (Exception e) {
                        log.warn("解析交通银行交易行失败: {}", line);
                    }
                }
            }
        }
        return wrapResult(records);
    }

    private boolean shouldSkip(String line) {
        if (line.contains("尊敬的") || line.contains("感谢您") || line.contains("本期账务说明")
                || line.contains("温馨提示") || line.contains("请妥善保管") || line.contains("交通银行信用卡客户")
                || line.contains("交通银行交易安全") || line.contains("风险与安全") || line.contains("透支日利率")
                || line.contains("最低还款额") || line.contains("交易币种") || line.contains("请您每月")
                || line.contains("下载买单吧") || line.contains("年") && line.contains("月") && line.contains("账单")
                || line.contains("第") && line.contains("页")) {
            return true;
        }
        return false;
    }

    private String cleanDescription(String desc) {
        // 移除 "主卡 卡号末四位 XXXX" 等信息
        desc = desc.replaceAll("主卡\\s*卡号末四位\\s*\\d{4}\\s*", "");
        // 移除多余空白
        desc = desc.replaceAll("\\s+", " ").trim();
        if (desc.length() > 255) {
            desc = desc.substring(0, 255);
        }
        return desc;
    }

    private String mapCategory(String desc) {
        if (desc == null || desc.isEmpty()) return "消费";
        String lower = desc.toLowerCase();
        if (lower.contains("还款") || lower.contains("转账还款")) return "信用卡还款";
        if (lower.contains("电费") || lower.contains("水费") || lower.contains("燃气")) return "居住生活";
        if (lower.contains("加油") || lower.contains("交通") || lower.contains("公交") || lower.contains("天府通")) return "交通出行";
        if (lower.contains("增值服务费") || lower.contains("保障") || lower.contains("保险")) return "金融保险";
        if (lower.contains("餐饮") || lower.contains("零售") || lower.contains("丰e足食")) return "餐饮饮食";
        if (lower.contains("娱乐") || lower.contains("会员") || lower.contains("视频")) return "娱乐休闲";
        return "消费";
    }
}

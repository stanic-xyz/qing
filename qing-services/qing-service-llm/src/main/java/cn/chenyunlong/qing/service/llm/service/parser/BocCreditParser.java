package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.Counterparty;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("BOC_CREDIT")
public class BocCreditParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "BOC_CREDIT";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    private static final DateTimeFormatter BOC_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 中国银行交易行格式:
    // MM/DD MM/DD CardLast4 Description(amount) Amount
    // 例如:
    // 01/21 01/22 2688 支付宝-重庆城市通卡支付 有限责任公司CHN 30.59
    // 01/31 02/01 2688 微信-JetBrainsCHN 1253.38
    // 07/19 07/19 2688 代付 103.86  <- 代付=收入
    private static final Pattern RECORD_PATTERN = Pattern.compile(
            "^(\\d{2}/\\d{2})\\s+(\\d{2}/\\d{2})\\s+(\\d{4})\\s+(.+?)\\s+(-?[\\d,]+\\.?\\d*)\\s*$"
    );

    // 匹配标题中的年份如：中国银行信用卡账单(2023年08月)
    private static final Pattern YEAR_PATTERN = Pattern.compile(".*\\((\\d{4})年.*\\).*");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        if (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");

                String year = "2023"; // 默认回退值
                String cardLast4 = "2688"; // 默认卡号后四位
                boolean isTransactionSection = false;

                // 第一遍：提取账单年份和卡号
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.contains("年") && trimmed.contains("月") && trimmed.contains("账单")) {
                        Matcher yearMatcher = YEAR_PATTERN.matcher(trimmed);
                        if (yearMatcher.matches()) {
                            year = yearMatcher.group(1);
                        }
                        Matcher cardMatcher = Pattern.compile("尾号(\\d{4})").matcher(trimmed);
                        if (cardMatcher.find()) {
                            cardLast4 = cardMatcher.group(1);
                        }
                    }
                    if (trimmed.contains("交易日") || trimmed.contains("Transaction Date")) {
                        isTransactionSection = true;
                    }
                }

                // 第二遍：解析交易行
                StringBuilder pendingDesc = new StringBuilder();

                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;

                    // 离开交易明细区域
                    if (isTransactionSection && (trimmed.contains("积分") || trimmed.contains("Loyalty")
                            || (trimmed.matches(".*第\\d+页.*") && trimmed.length() < 30))) {
                        isTransactionSection = false;
                        continue;
                    }

                    if (!isTransactionSection) {
                        if (trimmed.contains("交易日") || trimmed.contains("Transaction Date")) {
                            isTransactionSection = true;
                        }
                        continue;
                    }

                    Matcher matcher = RECORD_PATTERN.matcher(trimmed);
                    if (matcher.find()) {
                        // 保存上一条记录
                        if (pendingDesc.length() > 0) {
                            String fullDesc = pendingDesc.toString().trim();
                            if (!fullDesc.isEmpty()) {
                                records.get(records.size() - 1).setRemark(fullDesc);
                            }
                        }
                        pendingDesc.setLength(0);

                        String txDateStr = matcher.group(1); // MM/DD
                        String postDateStr = matcher.group(2);
                        String cardNum = matcher.group(3);
                        String descAmount = matcher.group(4).trim();
                        String amountStr = matcher.group(5).replace(",", "");

                        try {
                            BigDecimal amount = new BigDecimal(amountStr);
                            String[] dateParts = txDateStr.split("/");
                            int month = Integer.parseInt(dateParts[0]);
                            int day = Integer.parseInt(dateParts[1]);
                            int txYear = Integer.parseInt(year);
                            // 如果月份大于当前月份，可能是上一年的账单（跨年）
                            java.time.LocalDate now = java.time.LocalDate.now();
                            if (txYear == now.getYear() && month > now.getMonthValue() + 1) {
                                txYear--;
                            }
                            String txTimeStr = String.format("%04d-%02d-%02d 00:00:00", txYear, month, day);

                            // 判断收支：代付/存入/退款 = 收入，其他 = 支出
                            TrasactionType txType = TrasactionType.EXPENSE;
                            if (descAmount.contains("代付") || descAmount.contains("存入") || descAmount.contains("还款") || descAmount.contains("退款")) {
                                txType = TrasactionType.INCOME;
                            }

                            TransactionRecord record = new TransactionRecord();
                            record.setTransactionTime(LocalDateTime.parse(txTimeStr, BOC_DATE_FORMAT));
                            record.setAmount(amount.abs());
                            record.setType(txType);
                            record.setStatus(TransactionStatusEnum.SUCCESS);
                            record.setAccountName("中国银行信用卡(" + cardNum + ")");
                            record.setAccountType(AccountType.CREDIT);
                            record.setRecordRole(RecordRoleEnum.TRACE);
                            record.setFundSource("中国银行信用卡(" + cardNum + ")");
                            record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                            record.setConfirmed(false);
                            record.setOriginalId("boc_" + year + txDateStr.replace("/", "") + "_" + cardNum);

                            // 清理商户描述: 移除 CHN 币种标注
                            String merchant = descAmount.replace("CHN", "").replace("USD", "").trim();
                            // 提取对手方（从描述中提取）
                            String cpName = extractCounterparty(merchant);
                            if (!cpName.isEmpty()) {
                                Counterparty cp = new Counterparty();
                                cp.setName(cpName);
                                record.setCounterparty(cp);
                            }
                            record.setMerchant(merchant);
                            record.setSubCategory(mapCategory(merchant));

                            records.add(record);
                        } catch (Exception e) {
                            log.warn("解析中国银行交易行失败: {}, 错误: {}", trimmed, e.getMessage());
                        }
                    } else if (isTransactionSection && !trimmed.matches(".*\\d{4}-\\d{2}-\\d{2}.*")
                            && !trimmed.matches(".*积分.*") && !trimmed.matches(".*第.*页.*")
                            && !trimmed.contains("欠款") && !trimmed.contains("存款/欠款余额")
                            && trimmed.length() < 200) {
                        // 多行描述的续行
                        if (pendingDesc.length() > 0) {
                            pendingDesc.append(" ");
                        }
                        pendingDesc.append(trimmed.replace("CHN", ""));
                    }
                }

                // 处理最后一条记录的 pending desc
                if (pendingDesc.length() > 0 && !records.isEmpty()) {
                    String finalDesc = pendingDesc.toString().trim();
                    if (!finalDesc.isEmpty()) {
                        records.get(records.size() - 1).setRemark(finalDesc);
                    }
                }
            }
        }
        return wrapResult(records);
    }

    private String extractCounterparty(String merchant) {
        if (merchant == null || merchant.isEmpty()) return "";
        // 格式如: "支付宝-重庆城市通卡支付" 或 "微信-JetBrains" -> 提取 - 后的部分
        int dashIdx = merchant.indexOf("-");
        if (dashIdx >= 0 && dashIdx < merchant.length() - 1) {
            String cp = merchant.substring(dashIdx + 1).trim();
            // 移除公司类型后缀
            cp = cp.replaceAll("(有限责任公司|股份有限公司|有限公司|Co\\.|Ltd\\.).*", "").trim();
            return cp;
        }
        return "";
    }

    private String mapCategory(String merchant) {
        if (merchant == null || merchant.isEmpty()) return "消费";
        String lower = merchant.toLowerCase();
        if (lower.contains("拼多多") || lower.contains("京东") || lower.contains("淘宝")) return "购物消费";
        if (lower.contains("饿了么") || lower.contains("拉扎斯") || lower.contains("美团") || lower.contains("餐饮")) return "餐饮饮食";
        if (lower.contains("微信")) return "消费";
        if (lower.contains("支付宝")) return "消费";
        if (lower.contains("城市通卡") || lower.contains("交通")) return "交通出行";
        return "消费";
    }
}

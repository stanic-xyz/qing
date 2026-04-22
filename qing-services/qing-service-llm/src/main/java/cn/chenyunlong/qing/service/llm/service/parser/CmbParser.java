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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("CMB")
public class CmbParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "CMB";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    private static final DateTimeFormatter CMB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 招商银行格式: 2019-12-06 CNY 1,000.00 1,000.00 网联收款 或者 -1,000.00
    // ¥ 负数 = 支出（消费），¥ 正数 = 收入（存入/代发）
    private static final Pattern PDF_LINE_PATTERN = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2})\\s+(\\w+)\\s+(-?[\\d,]+\\.?\\d*)\\s+([\\d,]+\\.?\\d*)\\s+(.+)$"
    );

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        if (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                String text = stripper.getText(document);
                String[] lines = text.split("\\r?\\n");

                String accountName = "招商银行储蓄卡";
                String cardLast4 = detectCardLast4(text, originalFilename);
                if (!cardLast4.isEmpty()) {
                    accountName = "招商银行储蓄卡(" + cardLast4 + ")";
                }

                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;

                    // 跳过表头等非数据行
                    if (shouldSkip(trimmed)) continue;

                    Matcher matcher = PDF_LINE_PATTERN.matcher(trimmed);
                    if (matcher.find()) {
                        String dateStr = matcher.group(1) + " 00:00:00";
                        String currency = matcher.group(2);
                        String amountStr = matcher.group(3).replace(",", "");
                        String balanceStr = matcher.group(4).replace(",", "");
                        String rest = matcher.group(5).trim();

                        try {
                            BigDecimal amount = new BigDecimal(amountStr);
                            BigDecimal balance = new BigDecimal(balanceStr);

                            TransactionRecord record = new TransactionRecord();
                            record.setTransactionTime(LocalDateTime.parse(dateStr, CMB_FORMAT));

                            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                                record.setType(TrasactionType.EXPENSE);
                                record.setAmount(amount.abs());
                            } else {
                                record.setType(TrasactionType.INCOME);
                                record.setAmount(amount);
                            }

                            record.setBalance(balance);

                            // 解析摘要和对手信息
                            parseAndSetDetails(record, rest);

                            record.setAccountName(accountName);
                            record.setAccountType(AccountType.DEBIT);
                            record.setRecordRole(RecordRoleEnum.PRIMARY);
                            record.setFundSource(accountName);
                            record.setStatus(TransactionStatusEnum.SUCCESS);
                            record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                            record.setConfirmed(false);

                            records.add(record);
                        } catch (Exception e) {
                            log.warn("解析招商银行交易行失败: {}, 错误: {}", trimmed, e.getMessage());
                        }
                    }
                }
            }
        } else {
            // TXT 解析（暂保留，基于GBK编码）
            try {
                String content = new String(inputStream.readAllBytes(), "GBK");
                String[] lines = content.split("\\r?\\n");
                for (String line : lines) {
                    if (shouldSkip(line.trim())) continue;
                    // 简单处理：跳过（需要更完整的TXT处理逻辑）
                }
            } catch (Exception e) {
                log.warn("解析招商银行TXT失败: {}", e.getMessage());
            }
        }
        return wrapResult(records);
    }

    private String detectCardLast4(String text, String filename) {
        // 从文件名中提取
        Matcher m = Pattern.compile("(\\d{4})").matcher(filename);
        if (m.find()) {
            return m.group(1);
        }
        // 从文本中提取
        m = Pattern.compile("(?:账号|卡号)[^\\d]*(\\d{4})").matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private boolean shouldSkip(String line) {
        if (line.isEmpty()) return true;
        if (line.startsWith("记账日期") || line.startsWith("Date") || line.startsWith("Transaction")
                || line.startsWith("Amount") || line.startsWith("Balance")
                || line.startsWith("Currency") || line.startsWith("户名") || line.startsWith("账户类型")
                || line.startsWith("申请时间") || line.startsWith("----------------------------------------------------")
                || line.matches("^\\d+/\\d+$") || line.matches(".*第\\d+/\\d+页.*")
                || line.startsWith("招商银行") || line.startsWith("China Merchants Bank")) {
            return true;
        }
        return false;
    }

    /**
     * 解析剩余内容，提取摘要类型、对手方、备注
     * 格式: 摘要类型 [对手信息] [客户摘要]
     * 例如: 网联收款
     * 例如: 快捷支付 财付通-深圳市腾讯计算机系统 125993558910201
     * 例如: 银联代付 陈云龙 6217003810043305864
     */
    private void parseAndSetDetails(TransactionRecord record, String rest) {
        if (rest == null || rest.isEmpty()) return;

        String summaryType = "";
        String counterpartyName = "";
        String remark = "";

        // 摘要类型关键词
        String[] summaryKeywords = {"网联收款", "快捷支付", "银联代付", "转账汇款", "账户结息",
                "理财申购", "理财赎回", "POS消费", "消费撤销", "信用卡还款",
                "活期转入朝朝盈", "朝朝盈转活期", "代发款项", "工资", "报销"};

        for (String kw : summaryKeywords) {
            if (rest.startsWith(kw)) {
                summaryType = kw;
                rest = rest.substring(kw.length()).trim();
                break;
            }
        }

        // 提取对手方（通常是 名称 + 卡号 的形式）
        Matcher cpMatcher = Pattern.compile("(.+?)\\s+(\\d{16,19})\\s*$").matcher(rest);
        if (cpMatcher.find()) {
            counterpartyName = cpMatcher.group(1).trim();
            remark = rest;
        } else {
            // 没有卡号，整段作为对手方或备注
            if (!rest.isEmpty()) {
                // 可能是纯对手方，也可能是纯备注
                remark = rest;
            }
        }

        // 设置摘要类型作为分类
        record.setSubCategory(summaryType);

        // 设置对手方
        if (!counterpartyName.isEmpty()) {
            Counterparty cp = new Counterparty();
            cp.setName(counterpartyName);
            record.setCounterparty(cp);
        }

        // 设置备注（包含所有剩余信息）
        if (remark.length() > 255) {
            remark = remark.substring(0, 255);
        }
        record.setRemark(remark);
    }
}

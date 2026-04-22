package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.FundTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("ALIPAY")
public class AlipayParser extends BaseFileParser {

    private static final Charset GBK = Charset.forName("GBK");
    private static final DateTimeFormatter ALIPAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 支付方式关键词映射
    private static final Pattern BALANCE_PATTERN = Pattern.compile("(余额|零钱|花呗|亲情卡|红包|亲密支付)");
    private static final Pattern CARD_PATTERN = Pattern.compile("(信用卡|储蓄卡|银行卡|余额宝|信用账户)");
    private static final Pattern BANK_PATTERN = Pattern.compile("(交通银行|建设银行|中国银行|招商银行|中信银行|平安银行|工商银行|农业银行|邮储银行|网商银行|浦发银行|民生银行|兴业银行|光大银行|广发银行|华夏银行)");

    @Override
    public String channelCode() {
        return "ALIPAY";
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, GBK)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 8) continue;

                String timeStr = line[0].trim();
                if (!timeStr.startsWith("20")) continue;

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(LocalDateTime.parse(timeStr, ALIPAY_FORMAT));

                    // 对手方
                    String counterpartyStr = line[2].trim();
                    String counterpartyAccount = line.length > 3 ? line[3].trim() : "";
                    String fullCounterparty = counterpartyStr;
                    if (!counterpartyAccount.isEmpty() && !"/".equals(counterpartyAccount) && !counterpartyAccount.equals(counterpartyStr)) {
                        fullCounterparty = counterpartyStr + " (" + counterpartyAccount + ")";
                    }
                    if (!counterpartyStr.isEmpty() && !"/".equals(counterpartyStr)) {
                        Counterparty cp = new Counterparty();
                        cp.setName(counterpartyStr);
                        record.setCounterparty(cp);
                    }

                    // 商户/商品说明
                    String merchant = line.length > 4 ? line[4].trim() : "";
                    record.setMerchant(merchant);

                    // 收/支
                    String incomeExpense = line.length > 5 ? line[5].trim() : "";
                    if ("收入".equals(incomeExpense)) {
                        record.setType(TrasactionType.INCOME);
                    } else if ("支出".equals(incomeExpense)) {
                        record.setType(TrasactionType.EXPENSE);
                    } else {
                        record.setType(TrasactionType.OTHER);
                    }

                    // 金额
                    String amountStr = line.length > 6 ? line[6].trim().replace("¥", "").replace(",", "") : "0";
                    try {
                        record.setAmount(new BigDecimal(amountStr));
                    } catch (NumberFormatException e) {
                        record.setAmount(BigDecimal.ZERO);
                    }

                    // 交易状态
                    String statusStr = line.length > 8 ? line[8].trim() : "";
                    record.setStatus(mapStatus(statusStr));

                    // 交易订单号
                    if (line.length > 9 && !line[9].trim().isEmpty() && !"/".equals(line[9].trim())) {
                        record.setOriginalId(line[9].trim());
                    }

                    // 资金来源/支付方式
                    String paymentMethod = "";
                    if (line.length > 7 && !line[7].trim().isEmpty() && !"/".equals(line[7].trim())) {
                        paymentMethod = line[7].trim();
                        record.setFundSource(paymentMethod);
                        record.setRecordRole(deduceRecordRole(paymentMethod));
                        record.setFundType(deduceFundType(paymentMethod));
                    } else {
                        record.setRecordRole(RecordRoleEnum.PRIMARY);
                    }

                    // 分类（基于支付方式和商户）
                    record.setSubCategory(mapCategory(merchant, counterpartyStr, paymentMethod, incomeExpense));

                    // 构建备注
                    StringBuilder remarkBuilder = new StringBuilder();
                    if (line.length > 10 && !line[10].trim().isEmpty() && !"/".equals(line[10].trim())) {
                        remarkBuilder.append("商家订单号: ").append(line[10].trim()).append("; ");
                    }
                    if (line.length > 11 && !line[11].trim().isEmpty() && !"/".equals(line[11].trim())) {
                        remarkBuilder.append("备注: ").append(line[11].trim());
                    }
                    String finalRemark = remarkBuilder.toString().trim();
                    if (finalRemark.endsWith(";")) {
                        finalRemark = finalRemark.substring(0, finalRemark.length() - 1);
                    }
                    record.setRemark(finalRemark);

                    // 原始数据 JSON
                    java.util.Map<String, String> rawMap = new java.util.HashMap<>();
                    rawMap.put("paymentMethod", line.length > 7 ? line[7].trim() : "");
                    rawMap.put("merchantOrderNo", line.length > 10 ? line[10].trim() : "");
                    rawMap.put("tradeOrderNo", line.length > 9 ? line[9].trim() : "");
                    rawMap.put("counterpartyAccount", counterpartyAccount);
                    rawMap.put("counterpartyName", counterpartyStr);
                    rawMap.put("goodsDescription", merchant);
                    record.setOriginalData(new ObjectMapper().writeValueAsString(rawMap));

                    record.setAccountName("支付宝");
                    record.setAccountType(AccountType.WALLET);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析支付宝账单行失败: {}, 错误: {}", Arrays.toString(line), e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private RecordRoleEnum deduceRecordRole(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) return RecordRoleEnum.PRIMARY;
        String lower = paymentMethod.toLowerCase();
        if (lower.contains("余额") || lower.contains("零钱") || lower.contains("花呗") || lower.contains("红包") || lower.contains("亲情卡")) {
            return RecordRoleEnum.PRIMARY;
        }
        if (lower.contains("信用卡") || lower.contains("储蓄卡") || lower.contains("余额宝") || lower.contains("信用账户")) {
            return RecordRoleEnum.TRACE;
        }
        return RecordRoleEnum.PRIMARY;
    }

    public FundTypeEnum deduceFundType(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) return FundTypeEnum.EXTERNAL;
        String lower = paymentMethod.toLowerCase();
        if (lower.contains("余额") || lower.contains("零钱") || lower.contains("花呗")) {
            return FundTypeEnum.INTERNAL;
        }
        return FundTypeEnum.EXTERNAL;
    }

    private String mapCategory(String merchant, String counterparty, String paymentMethod, String incomeExpense) {
        String combined = (merchant != null ? merchant : "") + " " + (counterparty != null ? counterparty : "") + " " + (paymentMethod != null ? paymentMethod : "");
        String lower = combined.toLowerCase();

        if (incomeExpense != null && incomeExpense.equals("收入")) {
            if (lower.contains("红包") || lower.contains("礼金") || lower.contains("转账")) return "人情往来";
            if (lower.contains("工资") || lower.contains("奖金") || lower.contains("报销") || lower.contains("代发")) return "工作收入";
            if (lower.contains("退款") || lower.contains("退款")) return "金融保险";
            return "其他收入";
        }

        if (lower.contains("餐饮") || lower.contains("美食") || lower.contains("外卖") || lower.contains("饿了么") || lower.contains("美团") || lower.contains("水果") || lower.contains("蔬菜") || lower.contains("超市") || lower.contains("生鲜") || lower.contains("零食") || lower.contains("早餐") || lower.contains("午餐") || lower.contains("晚餐") || lower.contains("农贸") || lower.contains("火锅") || lower.contains("奶茶") || lower.contains("咖啡")) {
            return "餐饮饮食";
        }
        if (lower.contains("停车") || lower.contains("加油") || lower.contains("高速") || lower.contains("ETC") || lower.contains("地铁") || lower.contains("公交") || lower.contains("打车") || lower.contains("滴滴") || lower.contains("天府通") || lower.contains("骑行")) {
            return "交通出行";
        }
        if (lower.contains("京东") || lower.contains("淘宝") || lower.contains("拼多多") || lower.contains("天猫") || lower.contains("网购") || lower.contains("商城")) {
            return "购物消费";
        }
        if (lower.contains("医疗") || lower.contains("医院") || lower.contains("挂号") || lower.contains("药品") || lower.contains("体检") || lower.contains("口腔") || lower.contains("保险")) {
            return "医疗健康";
        }
        if (lower.contains("房租") || lower.contains("水电") || lower.contains("物业") || lower.contains("电费") || lower.contains("水费") || lower.contains("燃气")) {
            return "居住生活";
        }
        if (lower.contains("游戏") || lower.contains("视频") || lower.contains("会员") || lower.contains("腾讯") || lower.contains("爱奇艺") || lower.contains("优酷") || lower.contains("B站") || lower.contains("抖音") || lower.contains("音乐")) {
            return "娱乐休闲";
        }
        if (lower.contains("话费") || lower.contains("流量") || lower.contains("宽带") || lower.contains("中国移动") || lower.contains("联通") || lower.contains("电信")) {
            return "通讯网络";
        }
        if (lower.contains("教育") || lower.contains("课程") || lower.contains("培训") || lower.contains("书籍") || lower.contains("学习")) {
            return "教育学习";
        }
        if (lower.contains("红包") || lower.contains("礼金") || lower.contains("生日") || lower.contains("请客") || lower.contains("礼物")) {
            return "人情往来";
        }
        if (lower.contains("转账") || lower.contains("还款") || lower.contains("白条") || lower.contains("利息")) {
            return "金融保险";
        }
        return "其他";
    }

    private TransactionStatusEnum mapStatus(String status) {
        if (status == null) return TransactionStatusEnum.FAILED;
        return switch (status) {
            case "成功", "交易成功", "已完成", "已退款", "解冻成功" -> TransactionStatusEnum.SUCCESS;
            case "失败", "交易关闭" -> TransactionStatusEnum.FAILED;
            case "处理中", "退款中", "等待付款" -> TransactionStatusEnum.PENDING;
            default -> TransactionStatusEnum.FAILED;
        };
    }
}

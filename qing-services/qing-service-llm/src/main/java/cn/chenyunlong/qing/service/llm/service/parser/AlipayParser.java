package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
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

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.util.List;

@Slf4j
@Component("ALIPAY")
public class AlipayParser extends BaseFileParser {
    // 支付宝CSV编码为GBK
    private static final Charset GBK = Charset.forName("GBK");
    private static final DateTimeFormatter ALIPAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, GBK)).build()) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                // 支付宝CSV有许多非数据行，列数不足的跳过，真实数据至少11列以上
                if (line.length < 8) continue;

                String timeStr = line[0].trim();
                // 过滤表头及页脚（交易时间通常以年份如 20xx 开头）
                if (!timeStr.startsWith("20")) {
                    continue;
                }

                try {
                    // 表头对应关系:
                    // [0]交易时间, [1]交易分类, [2]交易对方, [3]对方账号, [4]商品说明, [5]收/支, [6]金额, [7]收/付款方式, [8]交易状态, [9]交易订单号, [10]商家订单号, [11]备注
                    TransactionRecord record = new TransactionRecord();
                    // todo 设置渠道
                    //  record.setChannel("ALIPAY");
                    record.setTransactionTime(LocalDateTime.parse(timeStr, ALIPAY_FORMAT));

                    // record.setCategory(line[1].trim());

                    String counterpartyStr = line[2].trim();
                    String counterpartyAccount = line[3].trim();
                    if (!counterpartyAccount.isEmpty() && !"/".equals(counterpartyAccount)) {
                        counterpartyStr = counterpartyStr + " (" + counterpartyAccount + ")";
                    }
                    if (!counterpartyStr.isEmpty()) {
                        cn.chenyunlong.qing.service.llm.entity.Counterparty cp = new cn.chenyunlong.qing.service.llm.entity.Counterparty();
                        cp.setName(counterpartyStr);
                        record.setCounterparty(cp);
                    }
                    record.setMerchant(line[4].trim());

                    // 状态: [8]
                    record.setStatus(mapStatus(line[8].trim()));

                    // 金额: [6]
                    String amountStr = line[6].trim().replace("¥", "").replace(",", "");
                    BigDecimal amount = new BigDecimal(amountStr);

                    // 收/支: [5]
                    String incomeExpense = line[5].trim();
                    if ("收入".equals(incomeExpense)) {
                        record.setType(TrasactionType.INCOME);
                    } else if ("支出".equals(incomeExpense)) {
                        record.setType(TrasactionType.EXPENSE);
                    } else {
                        record.setType(TrasactionType.OTHER);
                    }

                    record.setAmount(amount);

                    // 交易订单号
                    if (line.length > 9) {
                        record.setOriginalId(line[9].trim());
                    }

                    // 构建丰富的备注信息
                    StringBuilder remarkBuilder = new StringBuilder();
                    if (line.length > 7 && !line[7].trim().isEmpty() && !"/".equals(line[7].trim())) {
                        String paymentMethod = line[7].trim();
                        remarkBuilder.append("付款方式: ").append(paymentMethod).append("; ");
                        record.setFundSource(paymentMethod);

                        // 智能判断 RecordRole
                        if (paymentMethod.contains("\u4f59\u989d") || paymentMethod.contains("\u96f6\u94b1") || paymentMethod.contains("\u82b1\u5457") || paymentMethod.contains("\u7ea2\u5305") || paymentMethod.contains("\u4eb2\u60c5\u5361")) {
                            record.setRecordRole(RecordRoleEnum.PRIMARY);
                        } else if (paymentMethod.contains("\u50a8\u84c4\u5361") || paymentMethod.contains("\u4fe1\u7528\u5361") || paymentMethod.contains("\u94f6\u884c")) {
                            record.setRecordRole(RecordRoleEnum.TRACE);
                        } else {
                            record.setRecordRole(RecordRoleEnum.PRIMARY); // 默认
                        }
                    } else {
                        record.setRecordRole(RecordRoleEnum.PRIMARY);
                    }
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

                    java.util.Map<String, String> rawMap = new java.util.HashMap<>();
                    rawMap.put("paymentMethod", line.length > 7 ? line[7].trim() : "");
                    rawMap.put("merchantOrderNo", line.length > 10 ? line[10].trim() : "");
                    rawMap.put("tradeOrderNo", line.length > 9 ? line[9].trim() : "");
                    rawMap.put("counterpartyAccount", line.length > 3 ? line[3].trim() : "");
                    rawMap.put("counterpartyName", line.length > 2 ? line[2].trim() : "");
                    rawMap.put("goodsDescription", line.length > 4 ? line[4].trim() : "");
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

    private TransactionStatusEnum mapStatus(String status) {
        return switch (status) {
            case "成功", "交易成功", "已完成", "已退款", "解冻成功" -> TransactionStatusEnum.SUCCESS;
            case "失败", "交易关闭" -> TransactionStatusEnum.FAILED;
            case "处理中", "退款中", "等待付款" -> TransactionStatusEnum.PENDING;
            default -> TransactionStatusEnum.FAILED;
        };
    }
}

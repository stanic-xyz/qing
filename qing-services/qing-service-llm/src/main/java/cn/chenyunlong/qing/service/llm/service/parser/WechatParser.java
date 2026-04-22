package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

@Slf4j
@Component("WECHAT")
public class WechatParser extends BaseFileParser {

    private static final DateTimeFormatter WECHAT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String channelCode() {
        return "WECHAT";
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            // 找到表头行
            int headerRowIdx = -1;
            for (int i = 0; i < Math.min(30, sheet.getPhysicalNumberOfRows()); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;
                String val = getCellValueAsString(firstCell);
                if (val.contains("交易时间")) {
                    headerRowIdx = i;
                    break;
                }
            }

            if (headerRowIdx < 0) {
                log.warn("微信账单: 未找到表头行");
                return wrapResult(records);
            }

            // 建立列索引映射
            Row headerRow = sheet.getRow(headerRowIdx);
            int[] colMap = new int[11]; // 0-10
            for (int idx = 0; idx <= 10 && idx < headerRow.getLastCellNum(); idx++) {
                Cell cell = headerRow.getCell(idx);
                colMap[idx] = -1;
                if (cell != null) {
                    String headerVal = getCellValueAsString(cell);
                    if (headerVal.contains("交易时间")) colMap[0] = idx;
                    else if (headerVal.contains("交易类型")) colMap[1] = idx;
                    else if (headerVal.contains("交易对方")) colMap[2] = idx;
                    else if (headerVal.contains("商品")) colMap[3] = idx;
                    else if (headerVal.contains("收/支")) colMap[4] = idx;
                    else if (headerVal.contains("金额")) colMap[5] = idx;
                    else if (headerVal.contains("支付方式") || headerVal.contains("资金来源")) colMap[6] = idx;
                    else if (headerVal.contains("当前状态")) colMap[7] = idx;
                    else if (headerVal.contains("交易单号")) colMap[8] = idx;
                    else if (headerVal.contains("商户单号")) colMap[9] = idx;
                    else if (headerVal.contains("备注")) colMap[10] = idx;
                }
            }

            for (int ri = headerRowIdx + 1; ri <= sheet.getLastRowNum(); ri++) {
                try {
                    Row row = sheet.getRow(ri);
                    if (row == null) continue;

                    Cell timeCell = row.getCell(colMap[0]);
                    if (timeCell == null) continue;

                    String timeStr = getCellValueAsString(timeCell);
                    if (!timeStr.startsWith("20")) continue;

                    TransactionRecord record = new TransactionRecord();
                    record.setTransactionTime(LocalDateTime.parse(timeStr, WECHAT_FORMAT));

                    // 交易对方/商户
                    String counterparty = getCellValueAsString(row.getCell(colMap[2])).trim();
                    String merchant = getCellValueAsString(row.getCell(colMap[3])).trim();

                    // 设置对手方
                    if (!counterparty.isEmpty() && !"/".equals(counterparty)) {
                        Counterparty cp = new Counterparty();
                        cp.setName(counterparty);
                        record.setCounterparty(cp);
                    }

                    // 设置商户
                    if (!merchant.isEmpty()) {
                        record.setMerchant(merchant);
                    }

                    // 金额
                    Cell amountCell = row.getCell(colMap[5]);
                    if (amountCell != null) {
                        String amountStr = "";
                        if (amountCell.getCellType() == CellType.NUMERIC) {
                            amountStr = String.valueOf(amountCell.getNumericCellValue());
                        } else {
                            amountStr = amountCell.getStringCellValue().replace("¥", "").replace(",", "").trim();
                        }
                        try {
                            record.setAmount(new BigDecimal(amountStr).abs());
                        } catch (NumberFormatException e) {
                            record.setAmount(BigDecimal.ZERO);
                        }
                    }

                    // 收/支
                    String direction = getCellValueAsString(row.getCell(colMap[4])).trim();
                    if ("支出".equals(direction)) {
                        record.setType(TrasactionType.EXPENSE);
                    } else if ("收入".equals(direction)) {
                        record.setType(TrasactionType.INCOME);
                    } else {
                        record.setType(TrasactionType.OTHER);
                    }

                    // 支付方式/资金来源
                    String paymentMethod = getCellValueAsString(row.getCell(colMap[6])).trim();
                    if (!paymentMethod.isEmpty() && !"/".equals(paymentMethod)) {
                        record.setFundSource(paymentMethod);
                        record.setRecordRole(deduceRecordRole(paymentMethod));
                        record.setFundType(deduceFundType(paymentMethod));
                    } else {
                        record.setRecordRole(RecordRoleEnum.PRIMARY);
                    }

                    // 状态
                    String status = getCellValueAsString(row.getCell(colMap[7])).trim();
                    record.setStatus(mapStatus(status));

                    // 分类
                    String category = mapCategory(counterparty, merchant, direction);
                    record.setSubCategory(category);

                    // 备注
                    String remark = getCellValueAsString(row.getCell(colMap[10])).trim();
                    record.setRemark(remark);

                    // 原始ID
                    String orderId = getCellValueAsString(row.getCell(colMap[8])).trim();
                    if (!orderId.isEmpty()) {
                        record.setOriginalId(orderId);
                    }

                    record.setAccountName("微信");
                    record.setAccountType(AccountType.WALLET);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析微信账单行失败: 第 {} 行, 错误: {}", ri, e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private RecordRoleEnum deduceRecordRole(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) return RecordRoleEnum.PRIMARY;
        String lower = paymentMethod.toLowerCase();
        if (lower.contains("零钱") || lower.contains("零钱通") || lower.contains("红包") || lower.contains("银行卡")) {
            return RecordRoleEnum.PRIMARY;
        }
        return RecordRoleEnum.PRIMARY;
    }

    private cn.chenyunlong.qing.service.llm.enums.FundTypeEnum deduceFundType(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) return cn.chenyunlong.qing.service.llm.enums.FundTypeEnum.EXTERNAL;
        String lower = paymentMethod.toLowerCase();
        if (lower.contains("零钱") || lower.contains("零钱通")) {
            return cn.chenyunlong.qing.service.llm.enums.FundTypeEnum.INTERNAL;
        }
        return cn.chenyunlong.qing.service.llm.enums.FundTypeEnum.EXTERNAL;
    }

    private String mapCategory(String counterparty, String merchant, String direction) {
        String combined = (counterparty != null ? counterparty : "") + " " + (merchant != null ? merchant : "");
        String lower = combined.toLowerCase();

        if ("收入".equals(direction)) {
            if (lower.contains("红包") || lower.contains("礼金")) return "人情往来";
            if (lower.contains("退款")) return "其他收入";
            return "其他";
        }

        if (lower.contains("餐饮") || lower.contains("外卖") || lower.contains("美团") || lower.contains("饿了么") || lower.contains("水果") || lower.contains("超市") || lower.contains("农贸") || lower.contains("早餐") || lower.contains("午餐") || lower.contains("晚餐")) {
            return "餐饮饮食";
        }
        if (lower.contains("交通") || lower.contains("停车") || lower.contains("打车") || lower.contains("滴滴") || lower.contains("地铁") || lower.contains("公交") || lower.contains("骑行") || lower.contains("顺风车")) {
            return "交通出行";
        }
        if (lower.contains("京东") || lower.contains("淘宝") || lower.contains("拼多多") || lower.contains("购物")) {
            return "购物消费";
        }
        if (lower.contains("医疗") || lower.contains("医院") || lower.contains("挂号") || lower.contains("药房") || lower.contains("药品")) {
            return "医疗健康";
        }
        if (lower.contains("娱乐") || lower.contains("游戏") || lower.contains("视频") || lower.contains("音乐") || lower.contains("会员")) {
            return "娱乐休闲";
        }
        if (lower.contains("话费") || lower.contains("流量")) {
            return "通讯网络";
        }
        if (lower.contains("转账") || lower.contains("红包") || lower.contains("还款")) {
            return "人情往来";
        }
        return "其他";
    }

    private TransactionStatusEnum mapStatus(String status) {
        if (status == null) return TransactionStatusEnum.PENDING;
        if (status.contains("支付成功") || status.contains("已全额退款") || status.contains("已收钱")) return TransactionStatusEnum.SUCCESS;
        if (status.contains("退款")) return TransactionStatusEnum.SUCCESS;
        return TransactionStatusEnum.PENDING;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}

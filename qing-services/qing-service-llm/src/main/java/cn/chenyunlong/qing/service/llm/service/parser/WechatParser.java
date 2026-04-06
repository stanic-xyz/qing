package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.util.List;

@Slf4j
@Component("WECHAT")
public class WechatParser extends BaseFileParser {
    private static final DateTimeFormatter WECHAT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                try {
                    Cell timeCell = row.getCell(0);
                    if (timeCell == null) continue;

                    String timeStr = "";
                    if (timeCell.getCellType() == CellType.STRING) {
                        timeStr = timeCell.getStringCellValue().trim();
                    }

                    // 过滤表头及非数据行
                    if (!timeStr.startsWith("20")) {
                        continue;
                    }

                    TransactionRecord record = new TransactionRecord();
                    // todo 设置渠道
                    record.setTransactionTime(LocalDateTime.parse(timeStr, WECHAT_FORMAT));
                    // 商品
                    Cell goodsCell = row.getCell(3);
                    if (goodsCell != null) {
                        record.setMerchant(goodsCell.getStringCellValue().trim());
                    }

                    // 金额
                    Cell amountCell = row.getCell(5);
                    if (amountCell != null) {
                        String amountStr = "";
                        if (amountCell.getCellType() == CellType.NUMERIC) {
                            amountStr = String.valueOf(amountCell.getNumericCellValue());
                        } else {
                            amountStr = amountCell.getStringCellValue().replace("¥", "").replace(",", "").trim();
                        }
                        record.setAmount(new BigDecimal(amountStr));
                    }

                    // 收/支
                    Cell typeCell = row.getCell(4);
                    if (typeCell != null) {
                        String type = typeCell.getStringCellValue().trim();
                        if ("支出".equals(type)) record.setType(TrasactionType.EXPENSE);
                        else if ("收入".equals(type)) record.setType(TrasactionType.INCOME);
                        else record.setType(TrasactionType.OTHER);
                    }

                    // 支付方式 (资金来源)
                    Cell paymentMethodCell = row.getCell(6);
                    if (paymentMethodCell != null && paymentMethodCell.getCellType() == CellType.STRING) {
                        String pm = paymentMethodCell.getStringCellValue().trim();
                        if (!pm.isEmpty() && !"/".equals(pm)) {
                            record.setFundSource(pm);
                        }
                    }

                    // 状态
                    Cell statusCell = row.getCell(7);
                    if (statusCell != null) {
                        String status = statusCell.getStringCellValue().trim();
                        // todo 状态映射
                        record.setStatus(mapStatus(status));
                    }

                    // 备注
                    Cell remarkCell = row.getCell(10);
                    if (remarkCell != null && remarkCell.getCellType() == CellType.STRING) {
                        record.setRemark(remarkCell.getStringCellValue().trim());
                    }

                    record.setAccountName("微信");
                    record.setAccountType(AccountType.WALLET);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);
                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析微信账单行失败: 第 {} 行, 错误: {}", row.getRowNum(), e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }

    private TransactionStatusEnum mapStatus(String status) {
        if (status.contains("支付成功") || status.contains("已全额退款") || status.contains("已收钱")) return TransactionStatusEnum.SUCCESS;
        if (status.contains("退款")) return TransactionStatusEnum.SUCCESS; // 简化处理
        return TransactionStatusEnum.PENDING;
    }
}

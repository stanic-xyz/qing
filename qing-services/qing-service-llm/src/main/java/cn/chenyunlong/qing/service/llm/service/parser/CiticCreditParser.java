package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.util.List;

@Slf4j
@Component("CITIC_CREDIT")
public class CiticCreditParser extends BaseFileParser {

    public static final String CHANNEL_CODE = "CITIC_CREDIT";

    @Override
    public String channelCode() {
        return CHANNEL_CODE;
    }

    private static final DateTimeFormatter CITIC_DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                try {
                    Cell timeCell = row.getCell(0);
                    if (timeCell == null) continue;

                    String timeStr = "";
                    if (timeCell.getCellType() == CellType.STRING) {
                        timeStr = timeCell.getStringCellValue().trim();
                    } else if (timeCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(timeCell)) {
                        timeStr = timeCell.getLocalDateTimeCellValue().format(CITIC_DATE_FORMAT);
                    }

                    // 中信信用卡通常包含 yyyy-MM-dd 格式的日期
                    if (!timeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        continue;
                    }

                    TransactionRecord record = new TransactionRecord();
                    // todo 设置渠道
                    // record.setChannel("CITIC_CREDIT");
                    record.setTransactionTime(LocalDateTime.parse(timeStr, CITIC_DATE_FORMAT));

                    // 获取交易描述
                    Cell descCell = row.getCell(2);
                    if (descCell != null) {
                        record.setMerchant(descCell.getStringCellValue().trim());
                    }

                    // 获取金额 (中信结算金额在第7列索引或交易金额在第6列)
                    Cell amountCell = row.getCell(7); // 结算金额
                    if (amountCell == null || amountCell.getCellType() == CellType.BLANK) {
                        amountCell = row.getCell(6); // 如果结算金额为空尝试取交易金额
                    }
                    if (amountCell != null) {
                        if (amountCell.getCellType() == CellType.NUMERIC) {
                            record.setAmount(BigDecimal.valueOf(amountCell.getNumericCellValue()));
                        } else {
                            String amountStr = amountCell.getStringCellValue().replace(",", "").trim();
                            if (!amountStr.isEmpty()) {
                                record.setAmount(new BigDecimal(amountStr));
                            }
                        }
                    }

                    if (record.getAmount() == null) continue;

                    record.setAccountName("中信银行信用卡");
                    record.setAccountType(AccountType.CREDIT);
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);
                    record.setStatus(TransactionStatusEnum.SUCCESS);

                    if (record.getMerchant() != null && (record.getMerchant().contains("还款") || record.getMerchant().contains("存入"))) {
                        record.setType(TrasactionType.INCOME);
                    } else {
                        record.setType(TrasactionType.EXPENSE);
                    }

                    records.add(record);
                } catch (Exception e) {
                    log.warn("解析中信信用卡流水失败: 第 {} 行, 错误: {}", row.getRowNum(), e.getMessage());
                }
            }
        }
        return wrapResult(records);
    }
}

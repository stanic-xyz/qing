package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;

import java.util.List;

@Component("BANK")
public class BankParser extends BaseFileParser {
    @Override
    public String channelCode() {
        return "OTHER";
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        // 根据文件名后缀选择解析方式
        if (originalFilename.endsWith(".xls") || originalFilename.endsWith(".xlsx")) {
            return parseExcel(inputStream, originalFilename);
        } else if (originalFilename.endsWith(".csv")) {
            return parseCsv(inputStream);
        } else if (originalFilename.endsWith(".txt")) {
            return parseTxt(inputStream);
        } else if (originalFilename.endsWith(".pdf")) {
            return parsePdf(inputStream);
        } else {
            throw new RuntimeException("不支持的银行文件格式");
        }
    }

    private ParseResult parseExcel(InputStream inputStream, String filename) throws Exception {
        Workbook workbook;
        if (filename.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheetAt(0);
        // 假设第一行是标题，后续为数据行
        List<TransactionRecord> records = new ArrayList<>();
        boolean firstRow = true;
        for (Row row : sheet) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            TransactionRecord record = new TransactionRecord();
            // todo 设置渠道
            // record.setChannel("BANK");
            // 根据银行常见的列顺序自定义映射
            // 这里假设：日期、摘要、金额、余额、对方账户...
            // 实际需要根据具体银行调整
            if (row.getCell(0) != null) {
                // 日期单元格可能是字符串或日期类型
                String dateStr = row.getCell(0).toString();
                // 尝试解析日期
                // 简化为当天日期
                record.setTransactionTime(LocalDateTime.now());
            }
            // 金额
            Cell amountCell = row.getCell(2);
            if (amountCell != null) {
                double val = amountCell.getNumericCellValue();
                record.setAmount(BigDecimal.valueOf(val));
            }
            // 支出/收入判断（可能通过正负号或另一列）
            record.setType(TrasactionType.EXPENSE); // 默认支出
            record.setAccountName("银行账户"); // 需要从文件名或内容提取
            record.setAccountType(AccountType.DEBIT);
            record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
            record.setConfirmed(false);
            record.setRecordRole(RecordRoleEnum.PRIMARY);
            records.add(record);
        }
        return wrapResult(records);
    }

    private ParseResult parseCsv(InputStream inputStream) {
        // 实现CSV解析
        return wrapResult(new ArrayList<>());
    }

    private ParseResult parseTxt(InputStream inputStream) {
        // 实现TXT解析，可能为固定宽度或分隔符
        return wrapResult(new ArrayList<>());
    }

    private ParseResult parsePdf(InputStream inputStream) throws Exception {
        // 使用PDFBox提取文本，然后解析
        // 示例代码：
        /*
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            // 按行解析text
        }
        */
        return wrapResult(new ArrayList<>());
    }
}

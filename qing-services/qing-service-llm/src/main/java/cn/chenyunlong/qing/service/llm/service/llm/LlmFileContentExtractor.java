package cn.chenyunlong.qing.service.llm.service.llm;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件内容提取器 - 支持 PDF/Excel/CSV/Text
 */
@Service
@Slf4j
public class LlmFileContentExtractor {

    private static final int MAX_PAGE_PER_BATCH = 10;
    private static final int MAX_ROWS_PER_BATCH = 100;
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("\\d{1,10}(?:\\.\\d{1,2})?");
    private static final Pattern DATETIME_PATTERN = Pattern.compile("\\d{4}[-/年]\\d{1,2}[-/月]\\d{1,2}[\\s日]*(?:\\d{1,2}[时:：]\\d{1,2}(?:[分:：]\\d{1,2})?)?");

    /**
     * 从 MultipartFile 提取文本内容
     */
    public String extract(byte[] fileContent, String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".pdf")) {
            return extractTextFromPdf(fileContent);
        } else if (lowerName.endsWith(".xlsx") || lowerName.endsWith(".xls")) {
            return extractTextFromExcel(fileContent);
        } else if (lowerName.endsWith(".csv")) {
            return extractTextFromCsv(fileContent);
        } else {
            return extractTextFromText(fileContent);
        }
    }

    /**
     * 从 PDF 提取文本（分页处理）
     */
    public String extractTextFromPdf(byte[] content) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content);
             PDDocument document = Loader.loadPDF(content)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);

            int pageCount = document.getNumberOfPages();
            StringBuilder result = new StringBuilder();

            for (int page = 1; page <= pageCount; page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(Math.min(page, pageCount));
                String pageText = stripper.getText(document);
                result.append(pageText).append("\n");

                if (page % MAX_PAGE_PER_BATCH == 0) {
                    log.debug("PDF extracted {} pages so far", page);
                }
            }

            return result.toString();
        } catch (Exception e) {
            log.error("Failed to extract text from PDF", e);
            return "";
        }
    }

    /**
     * 从 Excel 提取文本
     */
    public String extractTextFromExcel(byte[] content) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
            Workbook workbook;
            if (ByteArrayInputStream.class.cast(bais).read() == 0x50) { // XLS
                bais.reset();
                workbook = new HSSFWorkbook(bais);
            } else {
                bais.reset();
                workbook = new XSSFWorkbook(bais);
            }

            StringBuilder result = new StringBuilder();
            DataFormatter formatter = new DataFormatter();

            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String sheetName = sheet.getSheetName();
                result.append("=== Sheet: ").append(sheetName).append(" ===\n");

                int rowCount = 0;
                for (Row row : sheet) {
                    if (rowCount++ > MAX_ROWS_PER_BATCH) {
                        log.debug("Excel row limit reached for sheet: {}", sheetName);
                        break;
                    }

                    StringBuilder rowText = new StringBuilder();
                    for (Cell cell : row) {
                        String cellValue = formatter.formatCellValue(cell);
                        if (!cellValue.isEmpty()) {
                            rowText.append(cellValue).append("\t");
                        }
                    }
                    if (rowText.length() > 0) {
                        result.append(rowText).append("\n");
                    }
                }
            }

            workbook.close();
            return result.toString();
        } catch (Exception e) {
            log.error("Failed to extract text from Excel", e);
            return "";
        }
    }

    /**
     * 从 CSV 提取文本
     */
    public String extractTextFromCsv(byte[] content) {
        try {
            String text = new String(content, StandardCharsets.UTF_8);
            return text;
        } catch (Exception e) {
            log.error("Failed to extract text from CSV", e);
            return "";
        }
    }

    /**
     * 从纯文本提取
     */
    public String extractTextFromText(byte[] content) {
        try {
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to extract text from file", e);
            return "";
        }
    }

    /**
     * 解析文本中的金额
     */
    public List<java.math.BigDecimal> parseAmounts(String text) {
        List<java.math.BigDecimal> amounts = new ArrayList<>();
        Matcher matcher = AMOUNT_PATTERN.matcher(text);
        while (matcher.find()) {
            try {
                amounts.add(new java.math.BigDecimal(matcher.group()));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return amounts;
    }

    /**
     * 解析文本中的时间
     */
    public List<LocalDateTime> parseDatetimes(String text) {
        List<LocalDateTime> datetimes = new ArrayList<>();
        Matcher matcher = DATETIME_PATTERN.matcher(text);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (matcher.find()) {
            try {
                String dateStr = matcher.group().replace("年", "-").replace("月", "-").replace("日", "");
                LocalDateTime dt = LocalDateTime.parse(dateStr, formatter);
                datetimes.add(dt);
            } catch (Exception e) {
                // ignore
            }
        }
        return datetimes;
    }
}

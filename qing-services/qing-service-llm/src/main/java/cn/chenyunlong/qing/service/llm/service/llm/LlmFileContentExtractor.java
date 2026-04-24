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
     * 从 CSV 提取文本（自动检测编码）
     */
    public String extractTextFromCsv(byte[] content) {
        try {
            return decodeWithCharsetDetection(content);
        } catch (Exception e) {
            log.error("Failed to extract text from CSV", e);
            return "";
        }
    }

    /**
     * 从纯文本提取（自动检测编码）
     */
    public String extractTextFromText(byte[] content) {
        try {
            return decodeWithCharsetDetection(content);
        } catch (Exception e) {
            log.error("Failed to extract text from file", e);
            return "";
        }
    }

    /**
     * 自动检测文件编码并解码
     * 支持：UTF-8、GBK、GB2312、UTF-16
     */
    private String decodeWithCharsetDetection(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }

        // 1. 检测 BOM
        if (content.length >= 3 && (content[0] & 0xFF) == 0xEF && (content[1] & 0xFF) == 0xBB && (content[2] & 0xFF) == 0xBF) {
            // UTF-8 BOM
            return new String(content, 3, content.length - 3, java.nio.charset.StandardCharsets.UTF_8);
        }
        if (content.length >= 2 && (content[0] & 0xFF) == 0xFF && (content[1] & 0xFF) == 0xFE) {
            // UTF-16 LE BOM
            return new String(content, 2, content.length - 2, java.nio.charset.StandardCharsets.UTF_16LE);
        }
        if (content.length >= 2 && (content[0] & 0xFF) == 0xFE && (content[1] & 0xFF) == 0xFF) {
            // UTF-16 BE BOM
            return new String(content, 2, content.length - 2, java.nio.charset.StandardCharsets.UTF_16BE);
        }

        // 2. 尝试 UTF-8 解码（如果成功且不含乱码特征字符，则认为是 UTF-8）
        String utf8Str = new String(content, java.nio.charset.StandardCharsets.UTF_8);
        if (!containsGarbledChars(utf8Str)) {
            return utf8Str;
        }

        // 3. 尝试 GBK（中文 Windows 常用）
        try {
            String gbkStr = new String(content, "GBK");
            if (!containsGarbledChars(gbkStr)) {
                log.debug("Detected GBK encoding, file size: {} bytes", content.length);
                return gbkStr;
            }
        } catch (Exception e) {
            // ignore
        }

        // 4. 尝试 GB2312（GBK 的子集）
        try {
            String gb2312Str = new String(content, "GB2312");
            if (!containsGarbledChars(gb2312Str)) {
                log.debug("Detected GB2312 encoding, file size: {} bytes", content.length);
                return gb2312Str;
            }
        } catch (Exception e) {
            // ignore
        }

        // 5. 兜底：使用 GBK（中文环境最常见）
        log.warn("Could not detect encoding confidently, falling back to GBK");
        return new String(content, "GBK");
    }

    /**
     * 检测字符串是否包含乱码字符
     */
    private boolean containsGarbledChars(String str) {
        if (str == null) return true;
        // 检测常见的乱码字符模式（� 或 CJK 替换字符）
        if (str.contains("\uFFFD")) {
            return true;
        }
        // 如果英文字符全变成了乱码的中文字符，大概率是编码错误
        int suspiciousCount = 0;
        for (int i = 0; i < str.length() && i < 200; i++) {
            char c = str.charAt(i);
            // 检测是否是 CJK 替换字符范围外但看起来像乱码的字符
            if (c >= 0x80 && c < 0x3000) {
                suspiciousCount++;
            }
        }
        // 前面200个字符中超过30个可疑字符，认为是乱码
        return suspiciousCount > 30;
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

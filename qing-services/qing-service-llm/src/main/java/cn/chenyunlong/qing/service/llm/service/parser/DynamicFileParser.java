package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.FieldMappingRule;
import cn.chenyunlong.qing.service.llm.dto.parser.FileMetadata;
import cn.chenyunlong.qing.service.llm.dto.parser.MetadataRule;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DynamicFileParser extends BaseFileParser {

    private final ParserConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DynamicFileParser(ParserConfig config) {
        this.config = config;
    }

    @Override
    public ParseResult parse(InputStream inputStream, String originalFilename) throws Exception {
        if ("CSV".equalsIgnoreCase(config.getFileType())) {
            return parseCsv(inputStream, originalFilename);
        } else if ("EXCEL".equalsIgnoreCase(config.getFileType())) {
            return parseExcel(inputStream, originalFilename);
        }
        throw new UnsupportedOperationException("目前动态解析器仅支持 CSV 和 EXCEL 格式");
    }

    private ParseResult parseCsv(InputStream inputStream, String originalFilename) throws Exception {
        Charset charset = Charset.forName(config.getEncoding() != null ? config.getEncoding() : "UTF-8");
        List<TransactionRecord> records = new ArrayList<>();
        FileMetadata metadata = new FileMetadata();

        List<FieldMappingRule> fieldRules = new ArrayList<>();
        if (config.getFieldMappingRules() != null && !config.getFieldMappingRules().isEmpty()) {
            fieldRules = objectMapper.readValue(config.getFieldMappingRules(), new TypeReference<List<FieldMappingRule>>() {});
        }

        List<MetadataRule> metaRules = new ArrayList<>();
        if (config.getMetadataRules() != null && !config.getMetadataRules().isEmpty()) {
            metaRules = objectMapper.readValue(config.getMetadataRules(), new TypeReference<List<MetadataRule>>() {});
        }

        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream, charset)).build()) {
            List<String[]> allLines = reader.readAll();

            // 1. 提取元数据
            for (MetadataRule rule : metaRules) {
                if (rule.getRowIndex() != null && rule.getRowIndex() < allLines.size()) {
                    String[] line = allLines.get(rule.getRowIndex());
                    if (rule.getColIndex() != null && rule.getColIndex() < line.length) {
                        String rawVal = line[rule.getColIndex()];
                        String extracted = extractRegex(rawVal, rule.getRegex());
                        setMetadataField(metadata, rule.getTargetField(), extracted);
                    }
                }
            }

            // 2. 解析交易记录
            int startRow = config.getSkipRows() != null ? config.getSkipRows() : 0;
            for (int i = startRow; i < allLines.size(); i++) {
                String[] line = allLines.get(i);
                // 简单过滤空行
                if (line == null || line.length == 0 || (line.length == 1 && line[0].trim().isEmpty())) {
                    continue;
                }

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setChannel(config.getChannel());
                    record.setAccountName(config.getName());
                    record.setReconciliationStatus("PENDING");
                    record.setConfirmed(false);

                    Map<String, Object> extData = new HashMap<>();

                    for (FieldMappingRule rule : fieldRules) {
                        if (rule.getSourceIndex() != null && rule.getSourceIndex() < line.length) {
                            String rawVal = line[rule.getSourceIndex()];
                            String cleanVal = applyCleanRules(rawVal, rule.getCleanRules());
                            if (cleanVal.isEmpty() && rule.getDefaultValue() != null) {
                                cleanVal = rule.getDefaultValue();
                            }

                            if (rule.getTargetField().startsWith("extData.")) {
                                String extKey = rule.getTargetField().substring("extData.".length());
                                extData.put(extKey, cleanVal);
                            } else {
                                setRecordField(record, rule.getTargetField(), cleanVal, rule.getDateFormat());
                            }
                        }
                    }

                    if (!extData.isEmpty()) {
                        record.setOriginalData(objectMapper.writeValueAsString(extData));
                    }

                    // 特殊处理收支类型映射
                    if (record.getType() != null) {
                        String t = record.getType();
                        if (t.contains("收") && !t.contains("支")) {
                            record.setType("INCOME");
                        } else if (t.contains("支") || t.contains("付") || t.contains("买")) {
                            record.setType("EXPENSE");
                        } else {
                            record.setType("OTHER");
                        }
                    }

                    // 如果没有解析出时间，或者核心字段缺失，则跳过
                    if (record.getTransactionTime() != null && record.getAmount() != null) {
                        records.add(record);
                    }
                } catch (Exception e) {
                    log.warn("解析第 {} 行失败: {}", i, Arrays.toString(line), e);
                }
            }
        }

        // 如果未通过规则提取元数据，则自动推算
        ParseResult result = wrapResult(records);
        if (metadata.getRecordCount() != null) {
            result.getMetadata().setRecordCount(metadata.getRecordCount());
        }
        if (metadata.getStartTime() != null) {
            result.getMetadata().setStartTime(metadata.getStartTime());
        }
        if (metadata.getEndTime() != null) {
            result.getMetadata().setEndTime(metadata.getEndTime());
        }

        return result;
    }

    private String extractRegex(String raw, String regex) {
        if (regex == null || regex.isEmpty()) return raw;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(raw);
        if (m.find()) {
            return m.groupCount() > 0 ? m.group(1) : m.group();
        }
        return raw;
    }

    private String applyCleanRules(String val, List<String> rules) {
        if (val == null) return "";
        if (rules == null || rules.isEmpty()) return val.trim();

        String result = val;
        for (String rule : rules) {
            result = switch (rule) {
                case "TRIM" -> result.trim();
                case "REMOVE_TABS" -> result.replace("\t", "");
                case "REMOVE_COMMAS" -> result.replace(",", "");
                case "REMOVE_RMB" -> result.replace("¥", "").replace("￥", "");
                default -> result;
            };
        }
        return result.trim();
    }

    private void setMetadataField(FileMetadata metadata, String field, String value) {
        if (value == null || value.isEmpty()) return;
        try {
            switch (field) {
                case "recordCount":
                    metadata.setRecordCount(Integer.parseInt(value.trim()));
                    break;
                case "startTime":
                    metadata.setStartTime(parseDateTime(value.trim(), "yyyy-MM-dd HH:mm:ss"));
                    break;
                case "endTime":
                    metadata.setEndTime(parseDateTime(value.trim(), "yyyy-MM-dd HH:mm:ss"));
                    break;
            }
        } catch (Exception e) {
            log.warn("设置元数据字段 {} 失败: {}", field, value, e);
        }
    }

    private void setRecordField(TransactionRecord record, String field, String value, String format) {
        if (value == null || value.isEmpty()) return;
        try {
            BeanWrapper wrapper = new BeanWrapperImpl(record);
            if ("amount".equals(field)) {
                wrapper.setPropertyValue(field, parseAmount(value));
            } else if ("transactionTime".equals(field)) {
                String f = (format != null && !format.isEmpty()) ? format : "yyyy-MM-dd HH:mm:ss";
                wrapper.setPropertyValue(field, parseDateTime(value, f));
            } else {
                wrapper.setPropertyValue(field, value);
            }
        } catch (Exception e) {
            log.warn("设置记录字段 {} 失败: {}", field, value, e);
        }
    }

    private ParseResult parseExcel(InputStream inputStream, String originalFilename) throws Exception {
        List<TransactionRecord> records = new ArrayList<>();
        FileMetadata metadata = new FileMetadata();

        List<FieldMappingRule> fieldRules = new ArrayList<>();
        if (config.getFieldMappingRules() != null && !config.getFieldMappingRules().isEmpty()) {
            fieldRules = objectMapper.readValue(config.getFieldMappingRules(), new TypeReference<List<FieldMappingRule>>() {});
        }

        List<MetadataRule> metaRules = new ArrayList<>();
        if (config.getMetadataRules() != null && !config.getMetadataRules().isEmpty()) {
            metaRules = objectMapper.readValue(config.getMetadataRules(), new TypeReference<List<MetadataRule>>() {});
        }

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // 默认处理第一个Sheet，可按需扩展
            int startRow = config.getSkipRows() != null ? config.getSkipRows() : 0;

            // 1. 提取元数据
            for (MetadataRule rule : metaRules) {
                Integer rowIdx = rule.getRowIndex();
                Integer colIdx = rule.getColIndex();
                if (rowIdx != null && colIdx != null) {
                    Row row = sheet.getRow(rowIdx);
                    if (row != null) {
                        Cell cell = row.getCell(colIdx);
                        if (cell != null) {
                            String rawVal = getCellStringValue(cell, null);
                            String extracted = extractRegex(rawVal, rule.getRegex());
                            setMetadataField(metadata, rule.getTargetField(), extracted);
                        }
                    }
                }
            }

            // 2. 解析交易记录
            DataFormatter dataFormatter = new DataFormatter(); // 用于统一获取单元格字符串
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 简单过滤空行：检查第一个非空单元格
                boolean isEmptyRow = true;
                for (Cell cell : row) {
                    if (cell != null && !getCellStringValue(cell, null).trim().isEmpty()) {
                        isEmptyRow = false;
                        break;
                    }
                }
                if (isEmptyRow) continue;

                try {
                    TransactionRecord record = new TransactionRecord();
                    record.setChannel(config.getChannel());
                    record.setAccountName(config.getName());
                    record.setReconciliationStatus("PENDING");
                    record.setConfirmed(false);

                    Map<String, Object> extData = new HashMap<>();

                    for (FieldMappingRule rule : fieldRules) {
                        Integer sourceIdx = rule.getSourceIndex();
                        if (sourceIdx != null && sourceIdx >= 0) {
                            Cell cell = row.getCell(sourceIdx);
                            String rawVal = (cell == null) ? "" : getCellStringValue(cell, rule.getDateFormat());
                            String cleanVal = applyCleanRules(rawVal, rule.getCleanRules());
                            if (cleanVal.isEmpty() && rule.getDefaultValue() != null) {
                                cleanVal = rule.getDefaultValue();
                            }

                            if (rule.getTargetField().startsWith("extData.")) {
                                String extKey = rule.getTargetField().substring("extData.".length());
                                extData.put(extKey, cleanVal);
                            } else {
                                setRecordField(record, rule.getTargetField(), cleanVal, rule.getDateFormat());
                            }
                        }
                    }

                    if (!extData.isEmpty()) {
                        record.setOriginalData(objectMapper.writeValueAsString(extData));
                    }

                    // 特殊处理收支类型映射（与CSV逻辑一致）
                    if (record.getType() != null) {
                        String t = record.getType();
                        if (t.contains("收") && !t.contains("支")) {
                            record.setType("INCOME");
                        } else if (t.contains("支") || t.contains("付") || t.contains("买")) {
                            record.setType("EXPENSE");
                        } else {
                            record.setType("OTHER");
                        }
                    }

                    if (record.getTransactionTime() != null && record.getAmount() != null) {
                        records.add(record);
                    }
                } catch (Exception e) {
                    log.warn("解析Excel第 {} 行失败: {}", i, e.getMessage(), e);
                }
            }
        }

        ParseResult result = wrapResult(records);
        if (metadata.getRecordCount() != null) {
            result.getMetadata().setRecordCount(metadata.getRecordCount());
        }
        if (metadata.getStartTime() != null) {
            result.getMetadata().setStartTime(metadata.getStartTime());
        }
        if (metadata.getEndTime() != null) {
            result.getMetadata().setEndTime(metadata.getEndTime());
        }
        return result;
    }

    /**
     * 获取单元格的字符串表示
     *
     * @param cell       Excel单元格
     * @param dateFormat 可选的日期格式（用于日期类型单元格的格式化）
     * @return 字符串值
     */
    private String getCellStringValue(Cell cell, String dateFormat) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日期类型：使用指定格式或默认格式
                    String format = (dateFormat != null && !dateFormat.isEmpty()) ? dateFormat : "yyyy-MM-dd HH:mm:ss";
                    LocalDateTime dateTime = cell.getLocalDateTimeCellValue();
                    return dateTime.format(DateTimeFormatter.ofPattern(format));
                } else {
                    // 数字：保留原始字符串形式，避免科学计数法
                    return new BigDecimal(String.valueOf(cell.getNumericCellValue())).toPlainString();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    // 尝试获取公式计算结果字符串
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    // 公式计算结果为数字
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }
}

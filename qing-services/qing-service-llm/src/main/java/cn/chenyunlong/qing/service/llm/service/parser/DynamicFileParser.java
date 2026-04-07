package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.FieldMappingRule;
import cn.chenyunlong.qing.service.llm.dto.parser.FileMetadata;
import cn.chenyunlong.qing.service.llm.dto.parser.MetadataRule;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
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
import java.time.Instant;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DynamicFileParser extends BaseFileParser {

    @Override
    public String channelCode() {
        throw new UnsupportedOperationException("channelCode() not implemented");
    }

    private final ParserConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScriptExecutorFactory scriptExecutorFactory;
    private final boolean strictMode;

    public DynamicFileParser(ParserConfig config, ScriptExecutorFactory scriptExecutorFactory) {
        this(config, scriptExecutorFactory, false);
    }

    /**
     * @param strictMode strict 模式下脚本执行/类型校验失败将直接抛出异常（用于配置测试）
     */
    public DynamicFileParser(ParserConfig config, ScriptExecutorFactory scriptExecutorFactory, boolean strictMode) {
        this.config = config;
        this.scriptExecutorFactory = scriptExecutorFactory;
        this.strictMode = strictMode;
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
                    // todo 设置渠道
                    //                    record.setChannel(config.getChannel());
                    record.setAccountName(config.getName());
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    Map<String, Object> extData = new HashMap<>();
                    Map<String, Object> rowContext = new HashMap<>();

                    for (FieldMappingRule rule : fieldRules) {
                        if (rule.getSourceIndex() != null && rule.getSourceIndex() < line.length) {
                            String rawVal = line[rule.getSourceIndex()];
                            String cleanVal = applyCleanRules(rawVal, rule.getCleanRules());
                            if (cleanVal.isEmpty() && rule.getDefaultValue() != null) {
                                cleanVal = rule.getDefaultValue();
                            }

                            if (rule.getTargetField() == null || rule.getTargetField().trim().isEmpty()) {
                                continue;
                            }

                            Object mappedValue = cleanVal;
                            if (shouldExecuteScript(rule)) {
                                Object scriptResult = executeRuleScript(rule, cleanVal, rowContext);
                                if (scriptResult instanceof Map<?, ?> resultMap) {
                                    if (rule.getTargetField().startsWith("extData.")) {
                                        throw new IllegalArgumentException("extData 字段不支持 Map 返回值脚本: " + rule.getTargetField());
                                    }
                                    for (Map.Entry<?, ?> entry : resultMap.entrySet()) {
                                        String field = String.valueOf(entry.getKey());
                                        Object v = entry.getValue();
                                        if (v == null) continue;
                                        setRecordField(record, field, v, null);
                                        rowContext.put(field, v);
                                    }
                                    continue;
                                }
                                mappedValue = scriptResult;
                            }

                            if (rule.getTargetField().startsWith("extData.")) {
                                if (mappedValue != null) {
                                    String extKey = rule.getTargetField().substring("extData.".length());
                                    extData.put(extKey, mappedValue);
                                }
                            } else {
                                if (mappedValue != null) {
                                    rowContext.put(rule.getTargetField(), mappedValue);
                                    setRecordField(record, rule.getTargetField(), mappedValue, rule.getDateFormat());
                                }
                            }
                        }
                    }

                    applyPostScriptIfEnabled(record, rowContext, extData);

                    if (!extData.isEmpty()) {
                        record.setOriginalData(objectMapper.writeValueAsString(extData));
                    }

                    // 如果没有解析出时间，或者核心字段缺失，则跳过
                    if (record.getTransactionTime() != null && record.getAmount() != null) {
                        records.add(record);
                    }
                } catch (Exception e) {
                    if (strictMode) {
                        throw e;
                    }
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

    private void setRecordField(TransactionRecord record, String field, Object value, String dateFormat) {
        if (value == null) return;
        if (value instanceof String s && s.trim().isEmpty()) return;

        BeanWrapper wrapper = new BeanWrapperImpl(record);
        if (!wrapper.isWritableProperty(field)) {
            if (strictMode) {
                throw new IllegalArgumentException("Unknown target field: " + field);
            }
            log.warn("Unknown target field: {}", field);
            return;
        }

        Class<?> targetType = wrapper.getPropertyType(field);
        Object coerced = coerceToPropertyType(targetType, value, dateFormat);
        wrapper.setPropertyValue(field, coerced);
    }

    private Object executeRuleScript(FieldMappingRule rule, String value, Map<String, Object> rowContext) {
        if (!shouldExecuteScript(rule)) return null;
        if (scriptExecutorFactory == null) {
            throw new IllegalStateException("Script executor factory is not available");
        }

        String language = normalizeScriptLanguage(rule.getScriptLanguage());
        if (!scriptExecutorFactory.isSupported(language)) {
            throw new IllegalArgumentException("Unsupported script language: " + language);
        }

        Map<String, Object> context = new HashMap<>(rowContext);
        context.put("value", value);
        context.put("targetField", rule.getTargetField());
        Object result = scriptExecutorFactory.execute(language, rule.getScriptRule(), context);
        if (result instanceof Map<?, ?>) {
            throw new IllegalArgumentException("Field-mapping script must return a scalar value; use postScript for Map results: " + rule.getTargetField());
        }
        return result;
    }

    private void applyPostScriptIfEnabled(TransactionRecord record, Map<String, Object> rowContext, Map<String, Object> extData) {
        if (record == null) return;
        if (scriptExecutorFactory == null) return;

        String script = config.getPostScript();
        if (script == null || script.trim().isEmpty()) {
            return;
        }

        Boolean enabled = config.getPostScriptEnabled();
        if (enabled != null && !enabled) {
            return;
        }

        String language = normalizeScriptLanguage(config.getPostScriptLanguage());
        if (!scriptExecutorFactory.isSupported(language)) {
            throw new IllegalArgumentException("Unsupported script language: " + language);
        }

        Map<String, Object> context = new HashMap<>(rowContext);
        context.put("record", record);
        context.put("row", rowContext);
        context.put("extData", extData);
        context.put("channel", config.getChannel());

        Object result = scriptExecutorFactory.execute(language, script, context);
        if (result == null) return;

        if (!(result instanceof Map<?, ?> resultMap)) {
            String msg = "Post script must return Map or null, actual=" + result.getClass().getSimpleName();
            if (strictMode) {
                throw new IllegalArgumentException(msg);
            }
            log.warn(msg);
            return;
        }

        for (Map.Entry<?, ?> entry : resultMap.entrySet()) {
            String field = String.valueOf(entry.getKey());
            Object v = entry.getValue();
            if (v == null) continue;

            if (field.startsWith("extData.")) {
                String extKey = field.substring("extData.".length());
                extData.put(extKey, v);
                continue;
            }

            setRecordField(record, field, v, null);
            rowContext.put(field, v);
        }
    }

    private boolean shouldExecuteScript(FieldMappingRule rule) {
        if (rule == null) return false;
        if (rule.getScriptRule() == null || rule.getScriptRule().trim().isEmpty()) return false;
        if (rule.getScriptEnabled() == null) {
            return true; // 兼容旧版：只要 scriptRule 有值就执行
        }
        return Boolean.TRUE.equals(rule.getScriptEnabled());
    }

    private String normalizeScriptLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            return "groovy";
        }
        return language.trim().toLowerCase();
    }

    private Object coerceToPropertyType(Class<?> targetType, Object value, String dateFormat) {
        if (targetType == null) {
            throw new IllegalArgumentException("Unknown target type");
        }
        if (value == null) return null;
        if (targetType.isInstance(value)) {
            return value;
        }

        if (String.class.equals(targetType)) {
            return String.valueOf(value);
        }
        if (BigDecimal.class.equals(targetType)) {
            return coerceBigDecimal(value);
        }
        if (LocalDateTime.class.equals(targetType)) {
            return coerceLocalDateTime(value, dateFormat);
        }
        if (Long.class.equals(targetType) || long.class.equals(targetType)) {
            return coerceLong(value);
        }
        if (Boolean.class.equals(targetType) || boolean.class.equals(targetType)) {
            return coerceBoolean(value);
        }
        if (targetType.isEnum()) {
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) targetType;
            return coerceEnum(enumType, value);
        }
        if (cn.chenyunlong.qing.service.llm.entity.Counterparty.class.equals(targetType)) {
            cn.chenyunlong.qing.service.llm.entity.Counterparty cp = new cn.chenyunlong.qing.service.llm.entity.Counterparty();
            cp.setName(String.valueOf(value));
            return cp;
        }
        if (cn.chenyunlong.qing.service.llm.entity.Category.class.equals(targetType)) {
            cn.chenyunlong.qing.service.llm.entity.Category cat = new cn.chenyunlong.qing.service.llm.entity.Category();
            cat.setName(String.valueOf(value));
            return cat;
        }

        throw new IllegalArgumentException("Script output type mismatch: expected=" + targetType.getSimpleName()
                + ", actual=" + value.getClass().getSimpleName());
    }

    private BigDecimal coerceBigDecimal(Object value) {
        if (value instanceof BigDecimal bd) return bd;
        if (value instanceof Number n) {
            return new BigDecimal(String.valueOf(n));
        }
        if (value instanceof String s) {
            return parseAmount(s);
        }
        throw new IllegalArgumentException("Cannot coerce to BigDecimal: " + value.getClass().getName());
    }

    private LocalDateTime coerceLocalDateTime(Object value, String dateFormat) {
        if (value instanceof LocalDateTime dt) return dt;
        if (value instanceof Date d) {
            return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        }
        if (value instanceof Number n) {
            Instant instant = Instant.ofEpochMilli(n.longValue());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        if (value instanceof String s) {
            String fVal = (dateFormat != null && !dateFormat.isEmpty()) ? dateFormat : "yyyy-MM-dd HH:mm:ss";
            return parseDateTime(s, fVal);
        }
        throw new IllegalArgumentException("Cannot coerce to LocalDateTime: " + value.getClass().getName());
    }

    private Long coerceLong(Object value) {
        if (value instanceof Long l) return l;
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) return Long.parseLong(s.trim());
        throw new IllegalArgumentException("Cannot coerce to Long: " + value.getClass().getName());
    }

    private Boolean coerceBoolean(Object value) {
        if (value instanceof Boolean b) return b;
        if (value instanceof Number n) return n.intValue() != 0;
        if (value instanceof String s) {
            String v = s.trim().toLowerCase();
            if ("true".equals(v) || "1".equals(v) || "yes".equals(v) || "y".equals(v)) return true;
            if ("false".equals(v) || "0".equals(v) || "no".equals(v) || "n".equals(v)) return false;
        }
        throw new IllegalArgumentException("Cannot coerce to Boolean: " + value.getClass().getName());
    }

    private Enum<?> coerceEnum(Class<? extends Enum<?>> enumType, Object value) {
        if (enumType.isInstance(value)) {
            return (Enum<?>) value;
        }
        if (value instanceof String s) {
            String name = s.trim();
            for (Enum<?> e : enumType.getEnumConstants()) {
                if (e.name().equalsIgnoreCase(name)) {
                    return e;
                }
            }
            if (enumType.getSimpleName().equals("TrasactionType")) {
                if (name.contains("\u6536\u5165") || name.contains("\u9000\u6b3e") || name.contains("\u8f6c\u5165") || name.contains("\u6c47\u5165") || name.contains("\u6536\u6b3e"))
                    return cn.chenyunlong.qing.service.llm.enums.TrasactionType.INCOME;
                if (name.contains("\u652f\u51fa") || name.contains("\u6d88\u8d39") || name.contains("\u8f6c\u51fa") || name.contains("\u6c47\u51fa") || name.contains("\u4ed8\u6b3e") || name.contains("\u4ee3\u6536"))
                    return cn.chenyunlong.qing.service.llm.enums.TrasactionType.EXPENSE;
                if (name.contains("\u8f6c\u8d26") || name.contains("\u63d0\u73b0") || name.contains("\u5145\u503c"))
                    return cn.chenyunlong.qing.service.llm.enums.TrasactionType.TRANSFER;
                return cn.chenyunlong.qing.service.llm.enums.TrasactionType.OTHER;
            }
            System.out.println("Failed to match TrasactionType for name: " + name + ", length=" + name.length());
            for (char c : name.toCharArray()) {
                System.out.println("char: " + c + " int: " + (int) c);
            }
            throw new IllegalArgumentException("Invalid enum value: " + s + ", enum=" + enumType.getSimpleName());
        }
        throw new IllegalArgumentException("Cannot coerce to Enum: " + value.getClass().getName());
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
                    // todo 设置渠道
                    //                    record.setChannel(config.getChannel());
                    record.setAccountName(config.getName());
                    record.setReconciliationStatus(ReconciliationStatusEnum.PENDING);
                    record.setConfirmed(false);

                    Map<String, Object> extData = new HashMap<>();
                    Map<String, Object> rowContext = new HashMap<>();

                    for (FieldMappingRule rule : fieldRules) {
                        Integer sourceIdx = rule.getSourceIndex();
                        if (sourceIdx != null && sourceIdx >= 0) {
                            Cell cell = row.getCell(sourceIdx);
                            String rawVal = (cell == null) ? "" : getCellStringValue(cell, rule.getDateFormat());
                            String cleanVal = applyCleanRules(rawVal, rule.getCleanRules());
                            if (cleanVal.isEmpty() && rule.getDefaultValue() != null) {
                                cleanVal = rule.getDefaultValue();
                            }

                            if (rule.getTargetField() == null || rule.getTargetField().trim().isEmpty()) {
                                continue;
                            }

                            Object mappedValue = cleanVal;
                            if (shouldExecuteScript(rule)) {
                                Object scriptResult = executeRuleScript(rule, cleanVal, rowContext);
                                if (scriptResult instanceof Map<?, ?> resultMap) {
                                    if (rule.getTargetField().startsWith("extData.")) {
                                        throw new IllegalArgumentException("extData 字段不支持 Map 返回值脚本: " + rule.getTargetField());
                                    }
                                    for (Map.Entry<?, ?> entry : resultMap.entrySet()) {
                                        String field = String.valueOf(entry.getKey());
                                        Object v = entry.getValue();
                                        if (v == null) continue;
                                        setRecordField(record, field, v, null);
                                        rowContext.put(field, v);
                                    }
                                    continue;
                                }
                                mappedValue = scriptResult;
                            }

                            if (rule.getTargetField().startsWith("extData.")) {
                                if (mappedValue != null) {
                                    String extKey = rule.getTargetField().substring("extData.".length());
                                    extData.put(extKey, mappedValue);
                                }
                            } else {
                                if (mappedValue != null) {
                                    rowContext.put(rule.getTargetField(), mappedValue);
                                    setRecordField(record, rule.getTargetField(), mappedValue, rule.getDateFormat());
                                }
                            }
                        }
                    }

                    applyPostScriptIfEnabled(record, rowContext, extData);

                    if (!extData.isEmpty()) {
                        record.setOriginalData(objectMapper.writeValueAsString(extData));
                    }

                    // 特殊处理收支类型映射（与CSV逻辑一致）
                    if (record.getType() != null) {
                        String t = record.getType().name();
                        String upper = t.trim().toUpperCase();
                        if ("INCOME".equals(upper) || "EXPENSE".equals(upper) || "OTHER".equals(upper)) {
                            record.setType(TrasactionType.valueOf(upper));
                        } else {
                            if (t.contains("收") && !t.contains("支")) {
                                record.setType(TrasactionType.INCOME);
                            } else if (t.contains("支") || t.contains("付") || t.contains("买")) {
                                record.setType(TrasactionType.EXPENSE);
                            } else {
                                record.setType(TrasactionType.OTHER);
                            }
                        }
                    }

                    if (record.getTransactionTime() != null && record.getAmount() != null) {
                        records.add(record);
                    }
                } catch (Exception e) {
                    if (strictMode) {
                        throw e;
                    }
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

package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.dto.parser.CommonBillRecord;
import cn.chenyunlong.qing.service.llm.dto.parser.LlmParseResponse;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseSummary;
import cn.chenyunlong.qing.service.llm.dto.parser.SuggestedCategory;
import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mock LLM 解析器 - MVP 阶段使用模拟数据
 * 模拟真实的 LLM 解析逻辑，返回结构化的账单数据
 */
@Service
@Slf4j
public class MockLlmParser {

    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    // Mock 分类 ID（实际项目中应从数据库查询）
    private static final Long CATEGORY_FOOD = 1L;
    private static final Long CATEGORY_DIGITAL = 3L;
    private static final Long CATEGORY_ENTERTAINMENT = 5L;
    private static final Long CATEGORY_TRANSPORT = 6L;
    private static final Long CATEGORY_SHOPPING = 7L;

    public MockLlmParser() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 解析账单文本
     *
     * @param prompt 包含原始账单文本和系统上下文的 Prompt
     * @param strategy 分类策略
     * @return JSON 格式的解析响应
     */
    public String parse(String prompt, CategoryStrategy strategy) {
        log.info("Mock LLM parsing with strategy: {}", strategy);

        try {
            // 模拟网络延迟
            Thread.sleep(1000);

            // 从 Prompt 中提取原始账单文本
            String rawText = extractRawTextFromPrompt(prompt);

            // 解析账单记录
            List<CommonBillRecord> records = parseRecordsFromText(rawText, strategy);

            // 生成解析汇总
            ParseSummary summary = generateSummary(records);

            // 生成建议分类
            List<SuggestedCategory> suggestedCategories = generateSuggestedCategories(records, strategy);

            // 构建响应
            LlmParseResponse response = new LlmParseResponse();
            response.setSuccess(true);
            response.setTaskId(UUID.randomUUID().toString());
            response.setRecords(records);
            response.setSummary(summary);
            response.setSuggestedNewCategories(suggestedCategories);
            response.setSuggestedNewAccounts(Collections.emptyList());
            response.setUnmatchedRecords(Collections.emptyList());

            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            log.error("Mock LLM parsing failed", e);
            LlmParseResponse errorResponse = LlmParseResponse.error("Mock parsing error: " + e.getMessage());
            try {
                return objectMapper.writeValueAsString(errorResponse);
            } catch (Exception ex) {
                return "{\"success\":false,\"errorMessage\":\"JSON serialization failed\"}";
            }
        }
    }

    /**
     * 从 Prompt 中提取原始账单文本
     */
    private String extractRawTextFromPrompt(String prompt) {
        // 简单地提取【原始账单文本】和【系统上下文】之间的内容
        int startIndex = prompt.indexOf("【原始账单文本】");
        int endIndex = prompt.indexOf("【系统上下文】");

        if (startIndex >= 0 && endIndex > startIndex) {
            return prompt.substring(startIndex + 8, endIndex).trim();
        }

        // 如果没有标记，直接返回原始 prompt
        return prompt;
    }

    /**
     * 从文本解析账单记录
     */
    private List<CommonBillRecord> parseRecordsFromText(String text, CategoryStrategy strategy) {
        List<CommonBillRecord> records = new ArrayList<>();
        String[] lines = text.split("\n");

        Pattern amountPattern = Pattern.compile("(\\d+\\.\\d{1,2})");
        Pattern datePattern = Pattern.compile("(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2})");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            CommonBillRecord record = new CommonBillRecord();

            // 解析金额
            Matcher amountMatcher = amountPattern.matcher(line);
            if (amountMatcher.find()) {
                try {
                    record.setAmount(new BigDecimal(amountMatcher.group(1)));
                } catch (NumberFormatException e) {
                    record.setAmount(BigDecimal.ZERO);
                }
            }

            // 解析时间
            Matcher dateMatcher = datePattern.matcher(line);
            if (dateMatcher.find()) {
                try {
                    String dateStr = dateMatcher.group(1).replace("/", "-");
                    record.setTransactionTime(LocalDateTime.parse(dateStr + " 00:00"));
                } catch (Exception e) {
                    record.setTransactionTime(LocalDateTime.now());
                }
            }

            // 根据关键词识别
            String lowerLine = line.toLowerCase();

            if (lowerLine.contains("京东") || lowerLine.contains("jd.com") || lowerLine.contains("jingdong")) {
                // 京东 -> 数码电子
                record.setPlatformSource("京东");
                record.setConsumptionType("数码电子");
                record.setCategoryId(CATEGORY_DIGITAL);
                record.setCategoryName("数码电子");
                record.setConfidence(BigDecimal.valueOf(generateConfidence()));
            } else if (lowerLine.contains("美团") || lowerLine.contains("meituan") ||
                       lowerLine.contains("外卖") || lowerLine.contains("饿了么")) {
                // 美团外卖 -> 餐饮美食
                record.setPlatformSource("美团");
                record.setConsumptionType("餐饮美食");
                record.setCategoryId(CATEGORY_FOOD);
                record.setCategoryName("餐饮美食");
                record.setConfidence(BigDecimal.valueOf(generateConfidence()));
            } else if (lowerLine.contains("抖音") || lowerLine.contains("douyin") ||
                       lowerLine.contains("直播") || lowerLine.contains("充值")) {
                // 抖音直播充值 -> 娱乐
                record.setPlatformSource("抖音");
                record.setConsumptionType("娱乐");
                record.setCategoryId(CATEGORY_ENTERTAINMENT);
                record.setCategoryName("娱乐");
                record.setConfidence(BigDecimal.valueOf(generateConfidence()));
                record.setNeedNewCategory(true);
            } else if (lowerLine.contains("地铁") || lowerLine.contains("公交") ||
                       lowerLine.contains("打车") || lowerLine.contains("滴滴")) {
                // 交通出行
                record.setPlatformSource("交通");
                record.setConsumptionType("交通出行");
                record.setCategoryId(CATEGORY_TRANSPORT);
                record.setCategoryName("交通出行");
                record.setConfidence(BigDecimal.valueOf(generateConfidence()));
            } else if (lowerLine.contains("淘宝") || lowerLine.contains("天猫") ||
                       lowerLine.contains("拼多多")) {
                // 购物
                record.setPlatformSource("电商");
                record.setConsumptionType("购物");
                record.setCategoryId(CATEGORY_SHOPPING);
                record.setCategoryName("购物");
                record.setConfidence(BigDecimal.valueOf(generateConfidence()));
            } else {
                // 默认分类
                record.setPlatformSource("未知");
                record.setConsumptionType("其他");
                record.setConfidence(BigDecimal.valueOf(generateConfidence()));
            }

            // 提取对手方
            record.setCounterparty(extractCounterparty(line));

            // 提取描述
            record.setDescription(line.length() > 100 ? line.substring(0, 100) : line);

            records.add(record);
        }

        return records;
    }

    /**
     * 生成置信度 0.7~0.95
     */
    private double generateConfidence() {
        return 0.7 + (0.25 * random.nextDouble());
    }

    /**
     * 提取对手方
     */
    private String extractCounterparty(String line) {
        // 简单实现：提取第一个括号内的内容或第一个空格前的词
        int bracketStart = line.indexOf('（');
        int bracketEnd = line.indexOf('）');
        if (bracketStart >= 0 && bracketEnd > bracketStart) {
            return line.substring(bracketStart + 1, bracketEnd);
        }

        bracketStart = line.indexOf('(');
        bracketEnd = line.indexOf(')');
        if (bracketStart >= 0 && bracketEnd > bracketStart) {
            return line.substring(bracketStart + 1, bracketEnd);
        }

        String[] parts = line.split("\\s+");
        return parts.length > 0 ? parts[0] : "未知";
    }

    /**
     * 生成解析汇总
     */
    private ParseSummary generateSummary(List<CommonBillRecord> records) {
        ParseSummary summary = new ParseSummary();
        summary.setTotalRecords(records.size());
        summary.setSuccessCount((int) records.stream().filter(r -> r.getCategoryId() != null).count());
        summary.setFailedCount((int) records.stream().filter(r -> r.getCategoryId() == null).count());

        double avgConfidence = records.stream()
                .filter(r -> r.getConfidence() != null)
                .mapToDouble(r -> r.getConfidence().doubleValue())
                .average()
                .orElse(0.0);

        summary.setAvgConfidence(Math.round(avgConfidence * 100) / 100.0);
        summary.setNeedReviewCount((int) records.stream().filter(r -> r.getConfidence() != null && r.getConfidence().doubleValue() < 0.9).count());
        summary.setInputTokens(records.size() * 50);
        summary.setOutputTokens(records.size() * 30);
        summary.setEstimatedCost(0.001 * records.size());

        // 统计平台来源和消费类型
        summary.setPlatformSources(records.stream()
                .map(CommonBillRecord::getPlatformSource)
                .filter(Objects::nonNull)
                .distinct()
                .toList());
        summary.setConsumptionTypes(records.stream()
                .map(CommonBillRecord::getConsumptionType)
                .filter(Objects::nonNull)
                .distinct()
                .toList());

        return summary;
    }

    /**
     * 生成建议分类（针对需要新建分类的情况）
     */
    private List<SuggestedCategory> generateSuggestedCategories(List<CommonBillRecord> records, CategoryStrategy strategy) {
        List<SuggestedCategory> suggestions = new ArrayList<>();

        // 检查 BY_CONSUMPTION_TYPE 策略下是否有需要新建分类的记录
        if (strategy == CategoryStrategy.BY_CONSUMPTION_TYPE) {
            boolean hasEntertainment = records.stream()
                    .anyMatch(r -> "娱乐".equals(r.getConsumptionType()));

            if (hasEntertainment) {
                SuggestedCategory suggestion = new SuggestedCategory();
                suggestion.setSuggestedName("直播娱乐");
                suggestion.setReason("检测到抖音直播充值等娱乐消费，建议新建分类");
                suggestion.setSourceStrategy(strategy.getCode());
                suggestion.setSampleDescriptions(new String[]{"抖音直播充值", "抖音打赏", "直播付费"});
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }
}

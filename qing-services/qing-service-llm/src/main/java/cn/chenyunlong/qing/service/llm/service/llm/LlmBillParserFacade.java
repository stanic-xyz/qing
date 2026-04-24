package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.dto.parser.*;
import cn.chenyunlong.qing.service.llm.entity.LlmParseDetail;
import cn.chenyunlong.qing.service.llm.entity.LlmParseRecord;
import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import cn.chenyunlong.qing.service.llm.repository.*;
import cn.chenyunlong.qing.service.llm.util.FileHashUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * LLM 账单解析Facade - 统一入口，编排解析流程
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmBillParserFacade {

    private final LlmFileContentExtractor fileContentExtractor;
    private final LlmParseContextLoader contextLoader;
    private final LlmParseResultCache resultCache;
    private final LlmParseTaskService taskService;
    private final MockLlmParser mockLlmParser;
    private final LlmPromptBuilder promptBuilder;
    private final LlmParseRecordRepository parseRecordRepository;
    private final LlmParseDetailRepository detailRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final CounterpartyRepository counterpartyRepository;

    private ObjectMapper objectMapper;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 同步解析
     */
    public LlmParseResponse parse(MultipartFile file, CategoryStrategy strategy) {
        return doParse(file, strategy, false);
    }

    /**
     * 异步解析入口 - 返回 taskId
     */
    public String parseAsync(MultipartFile file, CategoryStrategy strategy) {
        String taskId = taskService.submitTask("LLM_PARSE");

        CompletableFuture.runAsync(() -> {
            try {
                taskService.updateProgress(taskId, 10);
                LlmParseResponse response = doParse(file, strategy, true);
                taskService.updateProgress(taskId, 90);

                // 保存解析记录
                saveParseRecord(taskId, response);

                taskService.complete(taskId);
            } catch (Exception e) {
                log.error("Async parse failed for task: {}", taskId, e);
                taskService.fail(taskId, e.getMessage());
            }
        });

        return taskId;
    }

    /**
     * 查询任务状态
     */
    public TaskStatusResponse getTaskStatus(String taskId) {
        return taskService.getStatus(taskId);
    }

    /**
     * 获取解析结果
     */
    public List<LlmParseDetailDTO> getParseResult(Long parseRecordId) {
        List<LlmParseDetail> details = detailRepository.findByParseRecordId(parseRecordId);
        return details.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取解析记录
     */
    public Optional<LlmParseRecord> getParseRecord(Long id) {
        return parseRecordRepository.findById(id);
    }

    /**
     * 获取解析记录 by taskId
     */
    public Optional<LlmParseRecord> getParseRecordByTaskId(String taskId) {
        return parseRecordRepository.findByTaskId(taskId);
    }

    /**
     * 核心解析逻辑
     */
    private LlmParseResponse doParse(MultipartFile file, CategoryStrategy strategy, boolean isAsync) {
        try {
            // 1. 计算文件 hash
            String fileHash = FileHashUtil.calculateMD5(file.getBytes());
            String cacheKey = LlmParseResultCache.buildCacheKey(fileHash, strategy.getCode(), "v1");

            // 2. 缓存检查
            if (!isAsync && resultCache.hasValid(cacheKey)) {
                log.info("Using cached parse result for file: {}", file.getOriginalFilename());
                LlmParseResponse cached = resultCache.get(cacheKey);
                if (cached != null) {
                    return cached;
                }
            }

            // 3. 文件内容提取
            byte[] fileContent = file.getBytes();
            String rawText = fileContentExtractor.extract(fileContent,
                    Objects.requireNonNull(file.getOriginalFilename()));

            if (rawText == null || rawText.isBlank()) {
                return LlmParseResponse.error("无法从文件中提取文本内容");
            }

            // 4. 加载系统上下文
            LlmParseContextLoader.SystemContext context = contextLoader.load();

            // 5. 构建 Prompt
            String prompt = promptBuilder.buildPrompt(rawText, context, strategy);

            // 6. 调用 Mock LLM 解析器
            String llmResponse = mockLlmParser.parse(prompt, strategy);

            // 7. 解析 LLM 响应
            LlmParseResponse response = parseLlmResponse(llmResponse);

            // 8. 缓存结果
            resultCache.put(cacheKey, response);

            return response;

        } catch (IOException e) {
            log.error("Failed to read file content", e);
            return LlmParseResponse.error("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("Parse failed", e);
            return LlmParseResponse.error("解析失败: " + e.getMessage());
        }
    }

    /**
     * 解析 LLM 返回的 JSON 响应
     */
    private LlmParseResponse parseLlmResponse(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, LlmParseResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse LLM response as JSON", e);
            LlmParseResponse errorResponse = new LlmParseResponse();
            errorResponse.setSuccess(false);
            errorResponse.setErrorMessage("LLM 响应解析失败: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * 保存解析记录
     */
    @Transactional
    public void saveParseRecord(String taskId, LlmParseResponse response) {
        LlmParseRecord record = new LlmParseRecord();
        record.setTaskId(taskId);
        record.setTaskStatus(response.isSuccess() ? "COMPLETED" : "FAILED");
        record.setProgress(100);

        if (response.getSummary() != null) {
            record.setTotalRecords(response.getSummary().getTotalRecords());
            record.setSuccessCount(response.getSummary().getSuccessCount());
            record.setFailedCount(response.getSummary().getFailedCount());
            record.setNeedReviewCount(response.getSummary().getNeedReviewCount());
            record.setInputTokens(response.getSummary().getInputTokens());
            record.setOutputTokens(response.getSummary().getOutputTokens());
            record.setEstimatedCost(BigDecimal.valueOf(response.getSummary().getEstimatedCost()));
        }

        if (!response.isSuccess()) {
            record.setErrorMessage(response.getErrorMessage());
        }

        LlmParseRecord saved = parseRecordRepository.save(record);

        // 保存详情
        if (response.getRecords() != null) {
            for (CommonBillRecord billRecord : response.getRecords()) {
                LlmParseDetail detail = new LlmParseDetail();
                detail.setParseRecordId(saved.getId());
                detail.setAmount(billRecord.getAmount());
                detail.setTransactionType(billRecord.getTransactionType());
                detail.setTransactionTime(billRecord.getTransactionTime());
                detail.setCounterparty(billRecord.getCounterparty());
                detail.setDescription(billRecord.getDescription());
                detail.setPaymentMethod(billRecord.getPaymentMethod());
                if (billRecord.getAccountId() != null) {
                    detail.setAccountId(billRecord.getAccountId());
                }
                detail.setAccountName(billRecord.getAccountName());
                detail.setTransactionNo(billRecord.getTransactionNo());
                detail.setCategoryId(billRecord.getCategoryId());
                detail.setCategoryName(billRecord.getCategoryName());
                detail.setConfidence(billRecord.getConfidence());
                detail.setPlatformSource(billRecord.getPlatformSource());
                detail.setConsumptionType(billRecord.getConsumptionType());
                detail.setParseStatus("SUCCESS");
                detail.setNeedReview(billRecord.getConfidence() != null &&
                        billRecord.getConfidence().compareTo(BigDecimal.valueOf(0.9)) < 0);

                detailRepository.save(detail);
            }
        }

        log.info("Saved parse record: {} with {} details", saved.getId(),
                response.getRecords() != null ? response.getRecords().size() : 0);
    }

    /**
     * 转换为 DTO
     */
    private LlmParseDetailDTO toDTO(LlmParseDetail detail) {
        LlmParseDetailDTO dto = new LlmParseDetailDTO();
        dto.setId(detail.getId());
        dto.setParseRecordId(detail.getParseRecordId());
        dto.setTransactionRecordId(detail.getTransactionRecordId());
        dto.setAmount(detail.getAmount());
        dto.setTransactionType(detail.getTransactionType());
        dto.setTransactionTime(detail.getTransactionTime());
        dto.setCounterparty(detail.getCounterparty());
        dto.setDescription(detail.getDescription());
        dto.setPaymentMethod(detail.getPaymentMethod());
        dto.setAccountId(detail.getAccountId());
        dto.setAccountName(detail.getAccountName());
        dto.setStatus(detail.getStatus());
        dto.setTransactionNo(detail.getTransactionNo());
        dto.setCategoryId(detail.getCategoryId());
        dto.setCategoryName(detail.getCategoryName());
        dto.setConfidence(detail.getConfidence());
        dto.setMatchNote(detail.getMatchNote());
        dto.setPlatformSource(detail.getPlatformSource());
        dto.setConsumptionType(detail.getConsumptionType());
        dto.setTags(detail.getTags());
        dto.setParseStatus(detail.getParseStatus());
        dto.setImportStatus(detail.getImportStatus());
        dto.setNeedReview(detail.getNeedReview());
        dto.setReviewed(detail.getReviewed());
        dto.setReviewedBy(detail.getReviewedBy());
        dto.setReviewedAt(detail.getReviewedAt());
        dto.setOriginalText(detail.getOriginalText());
        dto.setVersion(detail.getVersion());
        return dto;
    }
}

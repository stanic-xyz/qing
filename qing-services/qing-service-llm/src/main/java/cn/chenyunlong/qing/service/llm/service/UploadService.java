package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.*;
import cn.chenyunlong.qing.service.llm.dto.parser.FileMetadata;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.*;
import cn.chenyunlong.qing.service.llm.repository.*;
import cn.chenyunlong.qing.service.llm.service.lock.BatchLockService;
import cn.chenyunlong.qing.service.llm.service.lock.Lock;
import cn.chenyunlong.qing.service.llm.service.lock.LockFactory;
import cn.chenyunlong.qing.service.llm.service.parser.DynamicFileParser;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import cn.chenyunlong.qing.service.llm.util.FileHashUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    private final List<FileParser> parserList; // 渠道名 -> 解析器 Bean

    private final ParserConfigRepository parserConfigRepository;

    private final TransactionRecordRepository transactionRepo;

    private final AccountRepository accountRepository;

    private final UploadFileRecordRepository uploadFileRepo;

    private final UnifiedDraftBatchRepository unifiedDraftBatchRepository;

    private final UnifiedDraftRecordRepository unifiedDraftRecordRepository;

    private final ScriptExecutorFactory scriptExecutorFactory;

    private final ReconciliationService reconciliationService;

    private final MatcherService matcherService;

    private final TransactionMatcherRepository matcherRepository;

    private final UnifiedDraftRecordRepository draftRecordRepository;

    private final UnifiedDraftBatchRepository draftBatchRepository;

    private final TransactionTemplate transactionTemplate;

    private final BatchLockService batchLockService;
    private final LockFactory lockFactory;
    private final Executor matchThreadPool;

    private final DraftCommitService commitService;


    // 缓存：解析器配置ID -> FileParser实例
    private final Map<String, FileParser> parserCache = new ConcurrentHashMap<>();


    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // 临时存储异步匹配任务状态
    private final Map<Long, MatchStatusResponse> matchStatusMap = new ConcurrentHashMap<>();
    private final BatchMatchService batchMatchService;

    /**
     * 根据解析器配置和文件名找到对应的内置解析器Bean
     */
    private FileParser getBuiltinParser(ParserConfig config, String ext) {
        Long configId = config.getId();

        if (StrUtil.isBlank(ext)) {
            throw new RuntimeException("文件后缀不能为空");
        }

        ext = ext.toUpperCase();
        String configKey = configId + ":" + ext;

        // 先从缓存中查找
        if (parserCache.containsKey(configKey)) {
            return parserCache.get(configKey);
        }

        String channelCode = config.getChannel().getCode();

        // 根据渠道代码和文件类型匹配解析器
        // 匹配逻辑：渠道代码 + 文件类型
        for (FileParser parser : parserList) {
            String parserChannel = parser.channelCode();

            // 渠道代码匹配
            if (!parserChannel.equalsIgnoreCase(channelCode)) {
                continue;
            }

            List<String> parserExtensions = parser.getMetaData().getSupportedFileExtension();
            if (!CollUtil.contains(parserExtensions, ext)) {
                continue;
            }
            // 缓存并返回
            parserCache.put(configKey, parser);
            return parser;
        }

        throw new RuntimeException(String.format(
                "找不到匹配的内置解析器: channel=%s, fileType=%s",
                channelCode, ext
        ));
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UploadBatchPreviewResponse> parseAndPreviewBatch(List<MultipartFile> files, Long parserId, Long accountId) throws Exception {
        Account targetAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("指定的账户不存在"));

        List<UploadBatchPreviewResponse> responses = new ArrayList<>();

        // 解析 parserId: "builtin:123" 或 "custom:456" 或 "ALIPAY" (兼容旧的)

        ParserConfig parserConfig = parserConfigRepository.findById(parserId).orElseThrow(() -> new RuntimeException("指定的解析器不存在"));

        for (MultipartFile file : files) {
            UploadBatchPreviewResponse previewResponse = parseFile(parserId, file, parserConfig, targetAccount);
            responses.add(previewResponse);
        }
        return responses;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UploadBatchPreviewResponse> parseAndPreview(MultipartFile file, Long parserId, Long accountId) throws Exception {
        Account targetAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("指定的账户不存在"));

        List<UploadBatchPreviewResponse> responses = new ArrayList<>();

        // 解析 parserId: "builtin:123" 或 "custom:456" 或 "ALIPAY" (兼容旧的)

        ParserConfig parserConfig = parserConfigRepository.findById(parserId).orElseThrow(() -> new RuntimeException("指定的解析器不存在"));
        UploadBatchPreviewResponse previewResponse = parseFile(parserId, file, parserConfig, targetAccount);
        responses.add(previewResponse);
        return responses;
    }

    private UploadBatchPreviewResponse parseFile(Long parserId, MultipartFile file, ParserConfig parserConfig, Account targetAccount) throws Exception {
        String originalFilename = file.getOriginalFilename();

        // 新的内置解析器：通过数据库ID查找
        // 根据渠道代码和文件类型找到对应的Bean
        FileParser parser;
        if (parserConfig.getIsBuiltIn()) {
            parser = getBuiltinParser(parserConfig, FileUtil.getSuffix(originalFilename));
        } else {
            parser = new DynamicFileParser(parserConfig, scriptExecutorFactory);
        }
        if (parser == null) {
            throw new RuntimeException("找不到有效的解析器: " + parserId);
        }

        String fileHash = FileHashUtil.calcMD5(file.getInputStream());
        long fileSize = file.getSize();

        ParseResult parseResult = parser.parse(file.getInputStream(), originalFilename);
        List<UnifiedDraftRecord> records = parseResult.getRecords();

        UploadFileRecord fileRecord = new UploadFileRecord();
        fileRecord.setAccount(targetAccount);
        fileRecord.setFileName(originalFilename);
        fileRecord.setFileHash(fileHash);
        fileRecord.setFileSize(fileSize);
        fileRecord.setStatus(FileUploadStatusEnum.UPLOADED);
        fileRecord.setParsedCount(records.size());
        FileMetadata metadata = parseResult.getMetadata();
        fileRecord.setStartTime(metadata.getStartTime());
        fileRecord.setEndTime(metadata.getEndTime());
        fileRecord.setTemplateVersion("v1");
        fileRecord.setChannel(targetAccount.getChannel() != null ? targetAccount.getChannel().getCode() : null);
        UploadFileRecord savedFileRecord = uploadFileRepo.save(fileRecord);

        String finalUploadId = String.valueOf(savedFileRecord.getId());

        // 统计收入/支出/转账数量
        int incomeCount = 0;
        int expenseCount = 0;
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (UnifiedDraftRecord record : records) {
            // 金额不可能为空
            assert record.getAmount() != null;
            switch (record.getDirection()) {
                case INCOME -> {
                    incomeCount++;
                    totalIncome = totalIncome.add(record.getAmount());
                }
                case EXPENSE -> {
                    expenseCount++;
                    totalExpense = totalExpense.add(record.getAmount());
                }
            }
        }

        // 按批次大小分组创建批次（默认200条一批）
        int BATCH_SIZE = 200;
        int batchIndex = 0;

        // 按交易时间从远到近排序
        records.sort(Comparator.comparing(UnifiedDraftRecord::getTransactionTime));

        for (int i = 0; i < records.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, records.size());
            List<UnifiedDraftRecord> batchRecords = records.subList(i, end);

            // 创建批次记录
            UnifiedDraftBatch batch = new UnifiedDraftBatch();
            batch.setAccount(targetAccount);
            batch.setAdapterType(AdapterTypeEnum.PARSER);
            batch.setBatchNo(String.format("%s_batch_%d", finalUploadId, batchIndex));
            batch.setUploadFile(fileRecord);
            batch.setStatus(DraftBatchStatusEnum.DRAFTED);
            batch.setTotalRecords(batchRecords.size());
            batch.setMatchedRecords(0);
            batch.setUnmatchedRecords(batchRecords.size());
            batch.setSuspiciousRecords(0);

            LocalDateTime minTime = null;
            LocalDateTime maxTime = null;

            // 获取当前批次的总金额和开始结束时间
            for (UnifiedDraftRecord record : batchRecords) {
                // 金额不可能为空
                switch (record.getDirection()) {
                    case INCOME -> {
                        incomeCount++;
                        totalIncome = totalIncome.add(record.getAmount());
                    }
                    case EXPENSE -> {
                        expenseCount++;
                        totalExpense = totalExpense.add(record.getAmount());
                    }
                }

                if (record.getTransactionTime() != null) {
                    if (minTime == null || record.getTransactionTime().isBefore(minTime)) {
                        minTime = record.getTransactionTime();
                    }
                    if (maxTime == null || record.getTransactionTime().isAfter(maxTime)) {
                        maxTime = record.getTransactionTime();
                    }
                }
            }
            batch.setTransactionStartTime(minTime);
            batch.setTransactionEndTime(maxTime);

            UnifiedDraftBatch savedBatch = draftBatchRepository.save(batch);

            // 立即刷新到数据库，确保ID生成并可用于后续关联
            log.info("Created batch: uploadId={}, batchNo={}, totalRecords={}", finalUploadId, savedBatch.getBatchNo(), savedBatch.getTotalRecords());

            // 设置每条记录的批次号
            for (UnifiedDraftRecord record : batchRecords) {
                record.setMatchStatus(DraftMatchStatusEnum.UNMATCHED);
                record.setBatch(savedBatch);
                record.setFileRecord(savedFileRecord);
            }
            batchIndex++;
        }

        // 并行写入统一草稿模型（新链路）
        UnifiedDraftBatch draftBatch = new UnifiedDraftBatch();
        draftBatch.setAccount(targetAccount);
        draftBatch.setBatchNo("parser-" + finalUploadId + "-" + System.currentTimeMillis());
        draftBatch.setAdapterType(AdapterTypeEnum.PARSER);
        draftBatch.setStatus(DraftBatchStatusEnum.DRAFTED);
        draftBatch.setProgress(60);
        draftBatch.setTotalRecords(records.size());
        draftBatch = unifiedDraftBatchRepository.save(draftBatch);
        unifiedDraftBatchRepository.flush();


        log.info("同步写入统一草稿批次 draftBatchId={}, records={}", draftBatch.getId(), records.size());
        int batchCount = (records.size() + BATCH_SIZE - 1) / BATCH_SIZE;
        unifiedDraftRecordRepository.saveAll(records);

        return UploadBatchPreviewResponse.builder()
                .uploadId(fileRecord.getId())
                .fileName(originalFilename)
                .parsedCount(records.size())
                .previewRecords(null)  // 不再返回明细，前端显示概览
                .build();
    }

    public UploadPreview getPreviewData(Long uploadId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionTime"));

        UploadFileRecord fileRecord = uploadFileRepo.findById(uploadId).orElseThrow(() -> new RuntimeException("上传记录不存在"));

        List<UnifiedDraftRecord> draftRecordList = draftRecordRepository.findAllByFileRecord(fileRecord);

        // 获取上传文件记录的状态
        List<PreviewRecordDTO> previewList = draftRecordList.stream()
                .map(draftRecord -> {
                    // 匹配批次信息
                    UnifiedDraftBatch batch = draftRecord.getBatch();
                    return PreviewRecordDTO.fromEntity(draftRecord);
                })
                .collect(Collectors.toList());

        return UploadPreview.builder()
                .uploadId(String.valueOf(uploadId))
                .previewRecords(previewList)
                .totalCount(previewList.size())
                .hasMore(false)
                .status(fileRecord.getStatus())
                .build();
    }

    public UploadBatchOverviewResponse getBatchOverview(Long uploadId) {

        UploadFileRecord fileRecord = uploadFileRepo.findById(uploadId).orElseThrow(() -> new RuntimeException("上传记录不存在"));

        Account account = fileRecord.getAccount();
        assert account != null;

        List<UnifiedDraftRecord> draftRecordList = draftRecordRepository.findAllByFileRecord(fileRecord);
        List<UnifiedDraftBatch> batches = draftBatchRepository.findAllByUploadFile(fileRecord);

        int incomeCount = 0;
        int expenseCount = 0;
        int transferCount = 0;

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        LocalDateTime minTime = null;
        LocalDateTime maxTime = null;

        for (UnifiedDraftRecord record : draftRecordList) {
            if (record.getDirection() == TransactionDirectionTypeEnum.INCOME) {
                incomeCount++;
                if (record.getAmount() != null) {
                    totalIncome = totalIncome.add(record.getAmount());
                }
            } else {
                expenseCount++;
                if (record.getAmount() != null) {
                    totalExpense = totalExpense.add(record.getAmount());
                }
            }

            if (record.getTransactionTime() != null) {
                if (minTime == null || record.getTransactionTime().isBefore(minTime)) {
                    minTime = record.getTransactionTime();
                }
                if (maxTime == null || record.getTransactionTime().isAfter(maxTime)) {
                    maxTime = record.getTransactionTime();
                }
            }
        }


        return UploadBatchOverviewResponse.builder()
                .uploadId(String.valueOf(uploadId))
                .fileName(fileRecord.getFileName())
                .fileSize(fileRecord.getFileSize())
                .totalRecords(draftRecordList.size())
                .incomeCount(incomeCount)
                .expenseCount(expenseCount)
                .transferCount(transferCount)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .transactionStartTime(minTime)
                .transactionEndTime(maxTime)
                .batchCount(batches.size())
                .accountName(account.getAccountName())
                .build();
    }

    public void startMatchingAsync(Long uploadId, List<Long> lockedTempIds) {

        // 1. 基础校验
        UploadFileRecord fileRecord = uploadFileRepo.findById(uploadId).orElseThrow(() -> new RuntimeException("上传记录不存在"));
        if (fileRecord.getStatus() != FileUploadStatusEnum.UPLOADED) {
            throw new RuntimeException("状态错误，当前状态不可匹配: " + fileRecord.getStatus());
        }

        // 更新整体状态为匹配中
        fileRecord.setStatus(FileUploadStatusEnum.MATCHING);
        uploadFileRepo.save(fileRecord);

        // 初始化响应状态对象
        MatchStatusResponse response = new MatchStatusResponse();
        response.setStatus("PROCESSING");
        matchStatusMap.put(uploadId, response);

        // 2. 准备外部数据：查询所有批次 + 历史记录快照（不可变）
        List<UnifiedDraftBatch> batches = unifiedDraftBatchRepository.findAllByUploadFile(fileRecord);
        if (batches.isEmpty()) {
            response.setStatus("COMPLETED");
            response.setPreview(getPreviewData(uploadId, 0, 100));
            fileRecord.setStatus(FileUploadStatusEnum.MATCHED);
            uploadFileRepo.save(fileRecord);
            return;
        }

        List<TransactionMatcher> rules = matcherRepository.findByIsActiveTrueOrderByPriorityDesc();

        // 历史记录快照（只读，用于匹配规则）
        List<TransactionRecord> historyRecords = transactionRepo.findAllByAccount(fileRecord.getAccount());
        // 转为不可变列表防止误修改
        List<TransactionRecord> immutableHistory = Collections.unmodifiableList(historyRecords);

        // 转变为不可变列表防止无修改
        List<TransactionMatcher> immutableRules = Collections.unmodifiableList(rules);


        // 3. 为每个批次提交异步任务
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (UnifiedDraftBatch batch : batches) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // 获取分布式锁（基于批次ID）
                Lock lock = lockFactory.getLock("batch_match_lock_" + batch.getId());
                boolean locked = false;
                try {
                    // 尝试获取锁，最多等待3秒
                    locked = lock.tryLock(3, TimeUnit.SECONDS);
                    if (!locked) {
                        log.warn("获取批次锁失败，跳过当前批次: batchId={}", batch.getId());
                        return;
                    }
                    // 调用批次匹配服务（内部独立事务）
                    batchMatchService.matchSingleBatch(batch.getId(), fileRecord.getAccount(), immutableHistory, lockedTempIds, immutableRules);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("批次匹配被中断: {}", batch.getId(), e);
                } catch (Exception e) {
                    log.error("批次匹配异常: {}", batch.getId(), e);
                } finally {
                    if (locked) {
                        lock.unlock();
                    }
                }
            }, matchThreadPool);

            futures.add(future);
        }

        // 4. 等待所有批次完成后，更新整体状态
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    // 检查是否所有批次都已结束（成功或失败）
                    List<UnifiedDraftBatch> finalBatches = draftBatchRepository.findAllByUploadFile(fileRecord);
                    boolean allFinished = finalBatches.stream()
                            .allMatch(draftBatch -> draftBatch.getStatus() == DraftBatchStatusEnum.MATCHED
                                    || draftBatch.getStatus() == DraftBatchStatusEnum.FAILED);
                    if (allFinished) {
                        // 如果有失败的批次，整体状态标记为 PARTIAL_MATCHED，否则 MATCHED
                        boolean hasFailed = finalBatches.stream()
                                .anyMatch(draftBatch -> draftBatch.getStatus() == DraftBatchStatusEnum.FAILED);
                        fileRecord.setStatus(hasFailed ? FileUploadStatusEnum.PARTIAL_MATCHED
                                : FileUploadStatusEnum.MATCHED);
                        uploadFileRepo.save(fileRecord);

                        response.setStatus("COMPLETED");
                        response.setPreview(getPreviewData(uploadId, 0, 100));
                        log.info("上传记录 {} 匹配完成，成功批次: {}, 失败批次: {}",
                                uploadId,
                                finalBatches.stream().filter(b -> b.getStatus() == DraftBatchStatusEnum.MATCHED).count(),
                                finalBatches.stream().filter(b -> b.getStatus() == DraftBatchStatusEnum.FAILED).count());
                    } else {
                        // 理论上 allOf 完成后所有任务都已结束，不应走到这里，但保留兜底
                        response.setStatus("COMPLETED");
                    }
                })
                .exceptionally(ex -> {
                    log.error("整体匹配过程发生异常", ex);
                    response.setStatus("FAILED");
                    response.setErrorMsg(ex.getMessage());
                    fileRecord.setStatus(FileUploadStatusEnum.MATCH_FAILED);
                    uploadFileRepo.save(fileRecord);
                    return null;
                });
    }

    public MatchStatusResponse getMatchStatus(Long uploadId) {
        MatchStatusResponse status = matchStatusMap.get(uploadId);
        if (status == null) {
            throw new RuntimeException("任务不存在或已过期");
        }
        return status;
    }

    public PreviewRecordDTO matchSingleRecord(String recordId, List<Long> ruleIds) {
        UnifiedDraftRecord record = draftRecordRepository.findById(Long.parseLong(recordId))
                .orElseThrow(() -> new RuntimeException("未找到该记录"));

        // 重置为初始状态（如果是重新匹配）
        record.setMatchStatus(DraftMatchStatusEnum.UNMATCHED);
        // 命中的匹配规则
        // record.setMatchRuleName(null);
        record.setIsModified(false);

        if (ruleIds != null && !ruleIds.isEmpty()) {
            List<TransactionMatcher> rules = matcherRepository.findAllById(ruleIds);
            rules.sort(Comparator.comparing(TransactionMatcher::getPriority).reversed());
            matcherService.applyMatchers(record, rules);
        } else {
            matcherService.applyMatchers(record);
        }

        // 这里只是预览，并不保存到数据库
        return PreviewRecordDTO.fromEntity(record);
    }

    public int importConfirmed(ImportRequest request) {
        Long uploadId = request.getUploadId();
        DraftCommitService.CommitResult commit = commitService.commit(uploadId);
        return commit.importedCount();
    }
}

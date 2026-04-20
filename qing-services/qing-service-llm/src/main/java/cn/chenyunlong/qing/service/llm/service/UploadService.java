package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.ImportRequest;
import cn.chenyunlong.qing.service.llm.dto.MatchStatusResponse;
import cn.chenyunlong.qing.service.llm.dto.PreviewRecordDTO;
import cn.chenyunlong.qing.service.llm.dto.UploadBatchPreviewResponse;
import cn.chenyunlong.qing.service.llm.dto.UploadPreview;
import cn.chenyunlong.qing.service.llm.dto.UploadBatchOverviewResponse;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.UploadBatch;
import cn.chenyunlong.qing.service.llm.enums.BatchStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadBatchRepository;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.DynamicFileParser;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import cn.chenyunlong.qing.service.llm.util.FileHashUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadService {

    @Resource
    private List<FileParser> parserList; // 渠道名 -> 解析器 Bean

    @Resource
    private ParserConfigRepository parserConfigRepository;

    @Resource
    private TransactionRecordRepository transactionRepo;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private UploadFileRecordRepository uploadFileRepo;

    @Resource
    private UploadBatchRepository uploadBatchRepo;

    @Resource
    private ScriptExecutorFactory scriptExecutorFactory;

    @Resource
    private ReconciliationService reconciliationService;

    @Resource
    private RuleEngineService ruleEngineService;

    @Resource
    private MatcherService matcherService;

    @Resource
    private TransactionMatcherRepository matcherRepository;

    // 缓存：解析器配置ID -> FileParser实例
    private final Map<String, FileParser> parserCache = new ConcurrentHashMap<>();


    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // 临时存储异步匹配任务状态
    private final Map<String, MatchStatusResponse> matchStatusMap = new ConcurrentHashMap<>();

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

        List<UploadBatchPreviewResponse> responses = new java.util.ArrayList<>();

        // 解析 parserId: "builtin:123" 或 "custom:456" 或 "ALIPAY" (兼容旧的)

        ParserConfig parserConfig = parserConfigRepository.findById(parserId).orElseThrow(() -> new RuntimeException("指定的解析器不存在"));


        for (MultipartFile file : files) {
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

            // 检查是否已处理
            Optional<UploadFileRecord> existing = uploadFileRepo.findByFileHash(fileHash);
            if (existing.isPresent()) {
                throw new RuntimeException("文件已上传过，请勿重复上传: " + originalFilename);
            }

            ParseResult parseResult = parser.parse(file.getInputStream(), originalFilename);
            List<TransactionRecord> records = parseResult.getRecords();

            LocalDateTime minTime = null;
            LocalDateTime maxTime = null;

            for (TransactionRecord record : records) {
                if (record.getTransactionTime() != null) {
                    if (minTime == null || record.getTransactionTime().isBefore(minTime)) {
                        minTime = record.getTransactionTime();
                    }
                    if (maxTime == null || record.getTransactionTime().isAfter(maxTime)) {
                        maxTime = record.getTransactionTime();
                    }
                }
            }


            UploadFileRecord fileRecord = new UploadFileRecord();
            fileRecord.setFileName(originalFilename);
            fileRecord.setFileHash(fileHash);
            fileRecord.setFileSize(fileSize);
            fileRecord.setStatus("UPLOADED");
            fileRecord.setParsedCount(records.size());
            fileRecord.setStartTime(minTime);
            fileRecord.setEndTime(maxTime);
            fileRecord.setTemplateVersion("v1");
            fileRecord.setChannel(targetAccount.getChannel() != null ? targetAccount.getChannel().getCode() : null);

            UploadFileRecord savedRecord = uploadFileRepo.save(fileRecord);
            String finalUploadId = String.valueOf(savedRecord.getId());

            // 统计收入/支出/转账数量
            int incomeCount = 0;
            int expenseCount = 0;
            int transferCount = 0;
            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;

            for (TransactionRecord record : records) {
                if (record.getType() == TrasactionType.INCOME) {
                    incomeCount++;
                    if (record.getAmount() != null) {
                        totalIncome = totalIncome.add(record.getAmount());
                    }
                } else if (record.getType() == TrasactionType.EXPENSE) {
                    expenseCount++;
                    if (record.getAmount() != null) {
                        totalExpense = totalExpense.add(record.getAmount());
                    }
                } else if (record.getType() == TrasactionType.TRANSFER) {
                    transferCount++;
                }
            }

            // 按批次大小分组创建批次（默认200条一批）
            int BATCH_SIZE = 200;
            int batchIndex = 0;

            // 按交易时间从远到近排序
            records.sort(Comparator.comparing(
                    r -> r.getTransactionTime() != null ? r.getTransactionTime() : LocalDateTime.MAX));

            for (int i = 0; i < records.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, records.size());
                List<TransactionRecord> batchRecords = records.subList(i, end);

                // 创建批次记录
                UploadBatch batch = new UploadBatch();
                batch.setUploadId(finalUploadId);
                batch.setBatchNo(String.format("%s_batch_%d", finalUploadId, batchIndex));
                batch.setStatus(BatchStatusEnum.PENDING);
                batch.setTotalRecords(batchRecords.size());
                batch.setMatchedRecords(0);
                batch.setUnmatchedRecords(batchRecords.size());
                batch.setSuspiciousRecords(0);
                batch.setTransactionStartTime(batchRecords.get(0).getTransactionTime());
                batch.setTransactionEndTime(batchRecords.get(batchRecords.size() - 1).getTransactionTime());
                UploadBatch savedBatch = uploadBatchRepo.save(batch);
                uploadBatchRepo.flush(); // 立即刷新到数据库
                log.info("Created batch: uploadId={}, batchNo={}, totalRecords={}", finalUploadId, savedBatch.getBatchNo(), savedBatch.getTotalRecords());

                // 设置每条记录的批次号
                for (TransactionRecord record : batchRecords) {
                    record.setSourceFile(originalFilename);
                    record.setUploadId(finalUploadId);
                    record.setBatchNo(batch.getBatchNo());
                    record.setAccount(targetAccount);
                    record.setChannel(targetAccount.getChannel());
                    record.setAccountName(targetAccount.getAccountName());
                    record.setAccountType(targetAccount.getAccountType());
                    record.setOriginalData(objectMapper.writeValueAsString(record));
                    record.setIsImported(false);
                }
                batchIndex++;
            }

            // 直接保存到数据库
            transactionRepo.saveAll(records);

            int batchCount = (records.size() + BATCH_SIZE - 1) / BATCH_SIZE;

            responses.add(UploadBatchPreviewResponse.builder()
                    .uploadId(finalUploadId)
                    .fileName(originalFilename)
                    .parsedCount(records.size())
                    .previewRecords(null)  // 不再返回明细，前端显示概览
                    .build());
        }
        return responses;
    }

    public UploadPreview getPreviewData(String uploadId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionTime"));
        Page<TransactionRecord> recordPage = transactionRepo.findByUploadId(uploadId, pageable);

        // 获取上传文件记录的状态
        UploadFileRecord fileRecord = uploadFileRepo.findById(Long.parseLong(uploadId)).orElse(null);
        String status = fileRecord != null ? fileRecord.getStatus() : null;

        List<PreviewRecordDTO> previewList = recordPage.getContent().stream()
                .map(r -> PreviewRecordDTO.fromEntity(r, String.valueOf(r.getId())))
                .collect(Collectors.toList());

        return UploadPreview.builder()
                .uploadId(uploadId)
                .previewRecords(previewList)
                .totalCount(recordPage.getTotalElements())
                .hasMore(recordPage.hasNext())
                .status(status)
                .build();
    }

    public UploadBatchOverviewResponse getBatchOverview(String uploadId) {
        UploadFileRecord fileRecord = uploadFileRepo.findById(Long.parseLong(uploadId))
                .orElseThrow(() -> new RuntimeException("上传记录不存在"));

        List<TransactionRecord> records = transactionRepo.findByUploadId(uploadId);
        List<UploadBatch> batches = uploadBatchRepo.findByUploadIdOrderByBatchNoAsc(uploadId);

        int incomeCount = 0;
        int expenseCount = 0;
        int transferCount = 0;
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        LocalDateTime minTime = null;
        LocalDateTime maxTime = null;

        for (TransactionRecord record : records) {
            if (record.getType() == TrasactionType.INCOME) {
                incomeCount++;
                if (record.getAmount() != null) {
                    totalIncome = totalIncome.add(record.getAmount());
                }
            } else if (record.getType() == TrasactionType.EXPENSE) {
                expenseCount++;
                if (record.getAmount() != null) {
                    totalExpense = totalExpense.add(record.getAmount());
                }
            } else if (record.getType() == TrasactionType.TRANSFER) {
                transferCount++;
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

        Account account = records.isEmpty() ? null : records.get(0).getAccount();
        String accountName = account != null ? account.getAccountName() : "未知账户";

        return UploadBatchOverviewResponse.builder()
                .uploadId(uploadId)
                .fileName(fileRecord.getFileName())
                .fileSize(fileRecord.getFileSize())
                .totalRecords(records.size())
                .incomeCount(incomeCount)
                .expenseCount(expenseCount)
                .transferCount(transferCount)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .transactionStartTime(minTime)
                .transactionEndTime(maxTime)
                .batchCount(batches.size())
                .accountName(accountName)
                .build();
    }

    public void startMatchingAsync(String uploadId, List<String> lockedTempIds) {
        List<TransactionRecord> records = transactionRepo.findByUploadId(uploadId);
        if (records.isEmpty()) {
            throw new RuntimeException("上传记录为空或已删除");
        }

        MatchStatusResponse status = new MatchStatusResponse();
        status.setStatus("PROCESSING");
        matchStatusMap.put(uploadId, status);

        CompletableFuture.runAsync(() -> {
            try {
                // 1. 应用规则引擎（老规则：基础映射）
                ruleEngineService.applyRules(records);

                // 2. 应用全新的高级匹配器引擎 (发现商户、平台、内部转账等)
                for (TransactionRecord record : records) {
                    if (lockedTempIds != null && lockedTempIds.contains(String.valueOf(record.getId()))) {
                        continue; // 跳过被锁定的记录
                    }
                    // 重置状态
                    record.setMatchStatus(MatchStatusEnum.ORIGINAL);
                    record.setMatchRuleName(null);
                    record.setIsModified(false);

                    matcherService.applyMatchers(record);
                }

                // 3. 更新数据库
                transactionRepo.saveAll(records);

                // 更新批次状态
                UploadFileRecord fileRecord = uploadFileRepo.findById(Long.parseLong(uploadId)).orElse(null);
                if (fileRecord != null) {
                    fileRecord.setStatus("MATCHING"); // 或 MATCHED
                    uploadFileRepo.save(fileRecord);
                }

                // 4. 构建最新的预览DTO
                List<PreviewRecordDTO> previewList = records.stream()
                        .map(r -> PreviewRecordDTO.fromEntity(r, String.valueOf(r.getId())))
                        .collect(Collectors.toList());

                UploadPreview preview = UploadPreview.builder()
                        .uploadId(uploadId)
                        .previewRecords(previewList)
                        .build();

                status.setStatus("COMPLETED");
                status.setPreview(preview);
            } catch (Exception e) {
                status.setStatus("FAILED");
                status.setErrorMsg(e.getMessage());
            }
        });
    }

    public MatchStatusResponse getMatchStatus(String uploadId) {
        MatchStatusResponse status = matchStatusMap.get(uploadId);
        if (status == null) {
            throw new RuntimeException("任务不存在或已过期");
        }
        return status;
    }

    public PreviewRecordDTO matchSingleRecord(String recordId, List<Long> ruleIds) {
        TransactionRecord record = transactionRepo.findById(Long.parseLong(recordId))
                .orElseThrow(() -> new RuntimeException("未找到该记录"));

        // 重置为初始状态（如果是重新匹配）
        record.setMatchStatus(MatchStatusEnum.ORIGINAL);
        record.setMatchRuleName(null);
        record.setIsModified(false);

        if (ruleIds != null && !ruleIds.isEmpty()) {
            List<cn.chenyunlong.qing.service.llm.entity.TransactionMatcher> rules = matcherRepository.findAllById(ruleIds);
            rules.sort(Comparator.comparing(cn.chenyunlong.qing.service.llm.entity.TransactionMatcher::getPriority).reversed());
            matcherService.applyMatchers(record, rules);
        } else {
            matcherService.applyMatchers(record);
        }

        // 这里只是预览，并不保存到数据库
        return PreviewRecordDTO.fromEntity(record, String.valueOf(record.getId()));
    }

    public int importConfirmed(ImportRequest request) {
        String uploadId = request.getUploadId();
        List<TransactionRecord> allRecords = transactionRepo.findByUploadId(uploadId);
        if (allRecords.isEmpty()) {
            throw new RuntimeException("上传记录为空或已删除");
        }

        // 过滤用户确认的记录
        List<TransactionRecord> toImport = new java.util.ArrayList<>();
        if (request.getConfirmedTempIds() != null && !request.getConfirmedTempIds().isEmpty()) {
            for (String tempIdStr : request.getConfirmedTempIds()) {
                try {
                    Long recordId = Long.parseLong(tempIdStr);
                    allRecords.stream()
                            .filter(r -> r.getId().equals(recordId))
                            .findFirst()
                            .ifPresent(toImport::add);
                } catch (NumberFormatException e) {
                    // ignore invalid ID
                }
            }
        } else {
            toImport.addAll(allRecords); // 兼容旧逻辑
        }

        // 应用用户修改
        if (request.getModifications() != null && !request.getModifications().isEmpty()) {
            Map<String, ImportRequest.ModifiedRecord> mods = request.getModifications().stream()
                    .collect(Collectors.toMap(ImportRequest.ModifiedRecord::getTempId, m -> m));
            for (TransactionRecord record : toImport) {
                ImportRequest.ModifiedRecord mod = mods.get(String.valueOf(record.getId()));
                if (mod != null) {
                    if (mod.getType() != null) {
                        record.setType(TrasactionType.valueOf(mod.getType().toUpperCase()));
                    }
                    record.setMerchant(mod.getMerchant());
                    record.setTargetAccountId(mod.getTargetAccountId());
                    if (mod.getTargetAccountId() != null) {
                        record.setType(TrasactionType.TRANSFER); // 强制为转账
                    }
                    record.setMatchStatus(MatchStatusEnum.MANUAL_EDITED);
                    record.setIsModified(true);
                }
            }
        }

        // 标记为已正式导入并统计规则正确率
        Map<String, cn.chenyunlong.qing.service.llm.entity.TransactionMatcher> matchersMap = matcherRepository.findAll().stream()
                .collect(Collectors.toMap(cn.chenyunlong.qing.service.llm.entity.TransactionMatcher::getName, m -> m));

        Map<Long, Account> accountsToUpdate = new java.util.HashMap<>();

        for (TransactionRecord record : toImport) {
            record.setIsImported(true);

            // 统计正确率
            if (record.getMatchRuleName() != null && !record.getMatchRuleName().isEmpty()) {
                cn.chenyunlong.qing.service.llm.entity.TransactionMatcher matcher = matchersMap.get(record.getMatchRuleName());
                if (matcher != null) {
                    matcher.setMatchCount(matcher.getMatchCount() == null ? 1 : matcher.getMatchCount() + 1);
                    if (record.getIsModified() == null || !record.getIsModified()) {
                        matcher.setSuccessCount(matcher.getSuccessCount() == null ? 1 : matcher.getSuccessCount() + 1);
                    }
                }
            }

            // 账户余额联动
            Account srcAccount = record.getAccount();
            if (srcAccount != null && record.getAmount() != null) {
                if (!accountsToUpdate.containsKey(srcAccount.getId())) {
                    accountsToUpdate.put(srcAccount.getId(), srcAccount);
                }
                Account acc = accountsToUpdate.get(srcAccount.getId());
                java.math.BigDecimal current = acc.getCurrentBalance() != null ? acc.getCurrentBalance() : java.math.BigDecimal.ZERO;

                if (TrasactionType.INCOME == record.getType()) {
                    acc.setCurrentBalance(current.add(record.getAmount()));
                } else if (TrasactionType.EXPENSE == record.getType()) {
                    acc.setCurrentBalance(current.subtract(record.getAmount()));
                } else if (TrasactionType.TRANSFER == record.getType()) {
                    acc.setCurrentBalance(current.subtract(record.getAmount())); // 源账户减去金额
                }

                // 处理转账的目标账户增加金额
                if (Objects.equals(TrasactionType.TRANSFER, record.getType()) && record.getTargetAccountId() != null) {
                    Account targetAcc = accountsToUpdate.get(record.getTargetAccountId());
                    if (targetAcc == null) {
                        targetAcc = accountRepository.findById(record.getTargetAccountId()).orElse(null);
                        if (targetAcc != null) {
                            accountsToUpdate.put(targetAcc.getId(), targetAcc);
                        }
                    }
                    if (targetAcc != null) {
                        java.math.BigDecimal targetCurrent = targetAcc.getCurrentBalance() != null ? targetAcc.getCurrentBalance() : java.math.BigDecimal.ZERO;
                        targetAcc.setCurrentBalance(targetCurrent.add(record.getAmount()));
                    }
                }
            }
        }
        matcherRepository.saveAll(matchersMap.values());
        if (!accountsToUpdate.isEmpty()) {
            accountRepository.saveAll(accountsToUpdate.values());
        }

        // 批量保存
        List<TransactionRecord> saved = transactionRepo.saveAll(toImport);

        // 删除未确认（被舍弃）的记录
        List<TransactionRecord> toDelete = new java.util.ArrayList<>(allRecords);
        toDelete.removeAll(toImport);
        if (!toDelete.isEmpty()) {
            transactionRepo.deleteAll(toDelete);
        }

        // 更新上传批次记录状态
        UploadFileRecord fileRecord = uploadFileRepo.findById(Long.parseLong(uploadId)).orElse(null);
        if (fileRecord != null) {
            fileRecord.setStatus("IMPORTED");
            fileRecord.setImportedCount(saved.size());
            fileRecord.setImportedAt(LocalDateTime.now());
            uploadFileRepo.save(fileRecord);
        }

        // 触发异步对账（可选）
        reconciliationService.autoReconcileForRecords(saved);

        return saved.size();
    }
}

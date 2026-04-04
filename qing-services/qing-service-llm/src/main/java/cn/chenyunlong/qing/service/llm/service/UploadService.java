package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.ImportRequest;
import cn.chenyunlong.qing.service.llm.dto.MatchStatusResponse;
import cn.chenyunlong.qing.service.llm.dto.PreviewRecordDTO;
import cn.chenyunlong.qing.service.llm.dto.UploadBatchPreviewResponse;
import cn.chenyunlong.qing.service.llm.dto.UploadPreview;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import cn.chenyunlong.qing.service.llm.entity.ParserConfig;
import cn.chenyunlong.qing.service.llm.repository.ParserConfigRepository;
import cn.chenyunlong.qing.service.llm.service.parser.DynamicFileParser;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.chenyunlong.qing.service.llm.service.script.ScriptExecutorFactory;
import cn.chenyunlong.qing.service.llm.util.FileHashUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UploadService {

    @Resource
    private Map<String, FileParser> parserMap; // 渠道名 -> 解析器 Bean

    @Resource
    private ParserConfigRepository parserConfigRepository;

    @Resource
    private TransactionRecordRepository transactionRepo;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private UploadFileRecordRepository uploadFileRepo;

    @Resource
    private ScriptExecutorFactory scriptExecutorFactory;

    @Resource
    private ReconciliationService reconciliationService;

    @Resource
    private RuleEngineService ruleEngineService;

    @Resource
    private MatcherService matcherService;


    @Resource
    private cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository matcherRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // 临时存储异步匹配任务状态
    private final Map<String, MatchStatusResponse> matchStatusMap = new ConcurrentHashMap<>();

    public List<UploadBatchPreviewResponse> parseAndPreviewBatch(List<MultipartFile> files, String parserId, Long accountId) throws Exception {
        Account targetAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("指定的账户不存在"));

        List<UploadBatchPreviewResponse> responses = new java.util.ArrayList<>();

        // 解析 parserId: "builtin:ALIPAY" 或 "custom:12" 或 "ALIPAY" (兼容旧的)
        FileParser parser = null;
        String actualChannel = parserId;

        if (parserId != null && parserId.startsWith("builtin:")) {
            actualChannel = parserId.substring(8);
            parser = parserMap.get(actualChannel.toUpperCase());
        } else if (parserId != null && parserId.startsWith("custom:")) {
            Long configId = Long.parseLong(parserId.substring(7));
            ParserConfig config = parserConfigRepository.findById(configId)
                    .orElseThrow(() -> new RuntimeException("找不到指定的自定义解析器"));
            actualChannel = config.getChannel();
            parser = new DynamicFileParser(config, scriptExecutorFactory);
        } else {
            // 兼容旧逻辑
            parser = parserMap.get(Objects.requireNonNull(parserId).toUpperCase());
        }

        if (parser == null) {
            throw new RuntimeException("找不到有效的解析器: " + parserId);
        }

        // 账号与渠道一致性校验：避免选错解析器导致数据落错账户
        if (targetAccount.getChannel() != null
                && actualChannel != null
                && !targetAccount.getChannel().trim().equalsIgnoreCase(actualChannel.trim())) {
            throw new RuntimeException("所选账号渠道(" + targetAccount.getChannel()
                    + ")与解析器渠道(" + actualChannel + ")不一致，请重新选择");
        }

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String fileHash = FileHashUtil.calcMD5(file.getInputStream());
            long fileSize = file.getSize();

            // 检查是否已处理
            Optional<UploadFileRecord> existing = uploadFileRepo.findByFileHash(fileHash);
            if (existing.isPresent()) {
                throw new RuntimeException("文件已上传过，请勿重复上传: " + originalFilename);
            }

            cn.chenyunlong.qing.service.llm.dto.parser.ParseResult parseResult = parser.parse(file.getInputStream(), originalFilename);
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
            fileRecord.setChannel(actualChannel);
            fileRecord.setStatus("UPLOADED");
            fileRecord.setParsedCount(records.size());
            fileRecord.setStartTime(minTime);
            fileRecord.setEndTime(maxTime);
            fileRecord.setTemplateVersion("v1");

            UploadFileRecord savedRecord = uploadFileRepo.save(fileRecord);
            String finalUploadId = String.valueOf(savedRecord.getId());

            for (TransactionRecord record : records) {
                record.setSourceFile(originalFilename);
                record.setUploadId(finalUploadId);
                record.setAccount(targetAccount);
                record.setAccountName(targetAccount.getAccountName());
                record.setAccountType(targetAccount.getAccountType());
                record.setOriginalData(objectMapper.writeValueAsString(record));
                record.setIsImported(false); // 关键：半成品隔离标记
            }

            // 直接保存到数据库
            transactionRepo.saveAll(records);

            List<PreviewRecordDTO> previewList = records.stream()
                    .map(r -> PreviewRecordDTO.fromEntity(r, String.valueOf(r.getId())))
                    .collect(Collectors.toList());

            responses.add(UploadBatchPreviewResponse.builder()
                    .uploadId(finalUploadId)
                    .fileName(originalFilename)
                    .parsedCount(records.size())
                    .previewRecords(previewList)
                    .build());
        }
        return responses;
    }

    public UploadPreview getPreviewData(String uploadId) {
        List<TransactionRecord> records = transactionRepo.findByUploadId(uploadId);
        List<PreviewRecordDTO> previewList = records.stream()
                .map(r -> PreviewRecordDTO.fromEntity(r, String.valueOf(r.getId())))
                .collect(Collectors.toList());
        return UploadPreview.builder()
                .uploadId(uploadId)
                .previewRecords(previewList)
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

        java.util.concurrent.CompletableFuture.runAsync(() -> {
            try {
                // 1. 应用规则引擎（老规则：基础映射）
                ruleEngineService.applyRules(records);

                // 2. 应用全新的高级匹配器引擎 (发现商户、平台、内部转账等)
                for (TransactionRecord record : records) {
                    if (lockedTempIds != null && lockedTempIds.contains(String.valueOf(record.getId()))) {
                        continue; // 跳过被锁定的记录
                    }
                    // 重置状态
                    record.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.ORIGINAL);
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
        record.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.ORIGINAL);
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
                    record.setType(mod.getType());
                    record.setMerchant(mod.getMerchant());
                    record.setTargetAccountId(mod.getTargetAccountId());
                    if (mod.getTargetAccountId() != null) {
                        record.setType("TRANSFER"); // 强制为转账
                    }
                    record.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.MANUAL_EDITED);
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

                if ("INCOME".equals(record.getType())) {
                    acc.setCurrentBalance(current.add(record.getAmount()));
                } else if ("EXPENSE".equals(record.getType())) {
                    acc.setCurrentBalance(current.subtract(record.getAmount()));
                } else if ("TRANSFER".equals(record.getType())) {
                    acc.setCurrentBalance(current.subtract(record.getAmount())); // 源账户减去金额
                }

                // 处理转账的目标账户增加金额
                if ("TRANSFER".equals(record.getType()) && record.getTargetAccountId() != null) {
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

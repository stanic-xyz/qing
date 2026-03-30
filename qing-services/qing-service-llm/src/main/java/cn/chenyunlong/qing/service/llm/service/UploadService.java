package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.ImportRequest;
import cn.chenyunlong.qing.service.llm.dto.PreviewRecordDTO;
import cn.chenyunlong.qing.service.llm.dto.UploadPreview;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import cn.chenyunlong.qing.service.llm.service.parser.FileParser;
import cn.chenyunlong.qing.service.llm.util.FileHashUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UploadService {

    @Resource
    private Map<String, FileParser> parserMap; // 渠道名 -> 解析器 Bean

    @Resource
    private TransactionRecordRepository transactionRepo;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private UploadFileRecordRepository uploadFileRepo;

    @Resource
    private ReconciliationService reconciliationService;

    @Resource
    private RuleEngineService ruleEngineService;

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // 临时存储解析结果（实际可用Redis或临时表）
    private Map<String, List<TransactionRecord>> tempStore = new ConcurrentHashMap<>();

    public UploadPreview parseAndPreview(MultipartFile file, String channel) throws Exception {
        // 1. 保存文件到临时目录，计算哈希
        String originalFilename = file.getOriginalFilename();
        String fileHash = FileHashUtil.calcMD5(file.getInputStream());
        long fileSize = file.getSize();

        // 2. 检查是否已处理（可选）
        Optional<UploadFileRecord> existing = uploadFileRepo.findByFileHash(fileHash);
        if (existing.isPresent()) {
            throw new RuntimeException("文件已上传过，请勿重复上传");
        }

        // 3. 获取对应渠道的解析器
        FileParser parser = parserMap.get(channel.toUpperCase());
        if (parser == null) {
            throw new RuntimeException("不支持的渠道: " + channel);
        }

        // 4. 解析文件，得到标准化记录列表（此时无数据库ID）
        List<TransactionRecord> records = parser.parse(file.getInputStream(), originalFilename);

        // 5. 应用规则引擎
        ruleEngineService.applyRules(records);

        // 6. 生成临时ID并存储
        String uploadId = UUID.randomUUID().toString();
        
        LocalDateTime minTime = null;
        LocalDateTime maxTime = null;
        
        for (int i = 0; i < records.size(); i++) {
            TransactionRecord record = records.get(i);
            record.setSourceFile(originalFilename); // 设置源文件名
            record.setUploadId(uploadId);
            record.setOriginalData(objectMapper.writeValueAsString(record)); // 保存原始快照
            
            if (record.getTransactionTime() != null) {
                if (minTime == null || record.getTransactionTime().isBefore(minTime)) {
                    minTime = record.getTransactionTime();
                }
                if (maxTime == null || record.getTransactionTime().isAfter(maxTime)) {
                    maxTime = record.getTransactionTime();
                }
            }
        }
        tempStore.put(uploadId, records);

        // 7. 构造预览DTO（去除敏感信息或只返回必要字段）
        List<PreviewRecordDTO> previewList = records.stream()
                .map(r -> PreviewRecordDTO.fromEntity(r, "temp_" + r.hashCode())) // 简单临时ID
                .collect(Collectors.toList());

        // 8. 保存上传记录
        UploadFileRecord fileRecord = new UploadFileRecord();
        fileRecord.setFileName(originalFilename);
        fileRecord.setFileHash(fileHash);
        fileRecord.setFileSize(fileSize);
        fileRecord.setChannel(channel);
        fileRecord.setStatus("UPLOADED");
        fileRecord.setParsedCount(records.size());
        fileRecord.setStartTime(minTime);
        fileRecord.setEndTime(maxTime);
        // 这里可以根据解析器返回额外的信息设置 templateVersion，目前暂存为 "v1"
        fileRecord.setTemplateVersion("v1");
        
        // 暂存 ID，为了后续能找到。如果依赖自增主键，可以先保存拿到 id
        UploadFileRecord savedRecord = uploadFileRepo.save(fileRecord);
        String finalUploadId = String.valueOf(savedRecord.getId());

        // 更新临时记录中的 uploadId 为数据库生成的真实 ID
        for (TransactionRecord record : records) {
            record.setUploadId(finalUploadId);
        }
        
        // 使用真正的 uploadId 作为 key
        tempStore.put(finalUploadId, records);

        return UploadPreview.builder()
                .uploadId(finalUploadId)
                .previewRecords(previewList)
                .build();
    }

    public int importConfirmed(ImportRequest request) {
        String uploadId = request.getUploadId();
        List<TransactionRecord> allRecords = tempStore.get(uploadId);
        if (allRecords == null) {
            throw new RuntimeException("上传会话已过期或不存在");
        }

        // 获取并校验关联账户
        Account targetAccount = null;
        if (request.getAccountId() != null) {
            targetAccount = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("指定的账户不存在"));
        } else {
            throw new RuntimeException("导入时必须选择一个关联账户");
        }

        // 过滤用户确认的记录（假设前端传了 confirmedTempIds）
        List<TransactionRecord> toImport = allRecords;
        if (request.getConfirmedTempIds() != null && !request.getConfirmedTempIds().isEmpty()) {
            // 根据临时ID过滤（实际可用映射关系）
            // 这里简化：直接使用全部
        }

        // 绑定账户信息到每一条流水记录
        for (TransactionRecord record : toImport) {
            record.setAccount(targetAccount);
            record.setAccountName(targetAccount.getAccountName()); // 冗余字段
            record.setAccountType(targetAccount.getAccountType());
        }

        // 批量保存
        List<TransactionRecord> saved = transactionRepo.saveAll(toImport);

        // 更新上传记录状态
        UploadFileRecord fileRecord = uploadFileRepo.findByFileName(allRecords.get(0).getSourceFile())
                .orElseThrow();
        fileRecord.setStatus("IMPORTED");
        fileRecord.setImportedCount(saved.size());
        fileRecord.setImportedAt(LocalDateTime.now());
        uploadFileRepo.save(fileRecord);

        // 清除临时存储
        tempStore.remove(uploadId);

        // 触发异步对账（可选）
        reconciliationService.autoReconcileForRecords(saved);

        return saved.size();
    }
}

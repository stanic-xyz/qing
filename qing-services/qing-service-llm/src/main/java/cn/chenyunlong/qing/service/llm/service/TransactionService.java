package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.service.llm.dto.transactions.CreateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.*;
import cn.chenyunlong.qing.service.llm.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRecordRepository transactionRepo;
    private final ChannelRepository channelRepo;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final CounterpartyRepository counterpartyRepository;

    /**
     * 创建单条流水
     */
    @Transactional(rollbackFor = Exception.class)
    public TransactionRecord create(@Valid CreateTransactionRecordDto dto) {
        return doCreate(dto, null);
    }

    /**
     * 批量创建流水（原子性，全部成功或全部失败）
     *
     * @param dtos 流水DTO列表，不能为空
     * @return 创建的流水实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<TransactionRecord> batchCreate(@NotEmpty(message = "流水列表不能为空") List<@Valid CreateTransactionRecordDto> dtos) {
        if (dtos.isEmpty()) {
            return Collections.emptyList();
        }
        // 统一批次号（若列表中所有DTO均未提供batchNo，则生成一个公共批次号）
        final String effectiveBatchNo = generateCommonBatchNo(dtos);

        List<TransactionRecord> records = new ArrayList<>(dtos.size());
        for (CreateTransactionRecordDto dto : dtos) {
            // 如果DTO没有批次号且我们生成了公共批次号，则使用公共批次号
            if (!StringUtils.hasText(dto.getBatchNo()) && effectiveBatchNo != null) {
                dto.setBatchNo(effectiveBatchNo);
            }
            records.add(doCreate(dto, effectiveBatchNo));
        }
        return records;
    }

    /**
     * 核心创建逻辑
     *
     * @param dto           流水DTO
     * @param forcedBatchNo 强制使用的批次号（批量调用时统一传入，单个调用时传null）
     */
    private TransactionRecord doCreate(CreateTransactionRecordDto dto, String forcedBatchNo) {

        // 1. 基础关联实体校验与加载
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new BusinessException("账户不存在，accountId=" + dto.getAccountId()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BusinessException("交易类别不存在，categoryId=" + dto.getCategoryId()));

        // 2. 业务逻辑校验
        validateTransactionBusinessRules(dto, account);

        // 3. 构建实体
        TransactionRecord record = new TransactionRecord();
        // 基础字段
        record.setAccount(account);
        record.setTransactionTime(dto.getTransactionTime() != null ? dto.getTransactionTime() : LocalDateTime.now());
        record.setOrderNo(dto.getOrderNo());
        record.setAmount(dto.getAmount());
        record.setBalance(dto.getBalance());
        record.setSummary(dto.getSummary());
        record.setDetail(dto.getDetail());
        record.setMerchant(dto.getMerchant());
        record.setFee(dto.getFee());
        record.setTags(dto.getTags() != null ? String.join(",", dto.getTags()) : null);
        record.setUploadId(dto.getUploadId());
        record.setMatchRuleName(dto.getMatchRuleName());
        record.setFundSource(dto.getFundSource());
        record.setFundSourceAccountId(dto.getFundSourceAccountId());
        record.setCategory(category);

        // 冗余字段填充（从关联实体获取或自动推断）
        record.setAccountName(account.getAccountName());
        record.setAccountType(account.getAccountType());
        // transactionType 处理：优先使用DTO传入，否则根据出入账类型推断
        record.setTransactionType(dto.getTransactionType());
        // subCategory 冗余：优先使用DTO传入，否则尝试从Category获取（假设Category有subCategoryName字段）
        // transactionRecordType 可选
        record.setTransactionRecordType(dto.getTransactionRecordType());
        TransactionDirectionTypeEnum directionType = inferTransactionDirection(dto.getAmount());
        record.setDirectionType(directionType);

        // 目标账户处理
        if (dto.getTargetAccountId() != null) {
            record.setTargetAccountId(dto.getTargetAccountId());
        }

        // 对手方处理：优先使用ID加载，否则使用文本
        if (dto.getCounterpartyId() != null) {
            Counterparty cp = counterpartyRepository.findById(dto.getCounterpartyId())
                    .orElseThrow(() -> new BusinessException("对手方不存在，counterpartyId=" + dto.getCounterpartyId()));
            record.setCounterparty(cp);
            record.setCounterpartyStr(cp.getName());
        } else if (StringUtils.hasText(dto.getCounterpartyStr())) {
            record.setCounterpartyStr(dto.getCounterpartyStr());
        }

        // 状态默认值
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : TransactionStatusEnum.SUCCESS);
        record.setReconciliationStatus(dto.getReconciliationStatus() != null ? dto.getReconciliationStatus() : ReconciliationStatusEnum.PENDING);
        record.setConfirmed(dto.getConfirmed() != null ? dto.getConfirmed() : false);
        record.setMatchStatus(dto.getMatchStatus() != null ? dto.getMatchStatus() : MatchStatusEnum.ORIGINAL);
        record.setRecordRole(dto.getRecordRole() != null ? dto.getRecordRole() : RecordRoleEnum.PRIMARY);
        record.setIsDeleted(false);
        record.setIsModified(false);
        record.setIsImported(true);   // 默认已导入

        // 批次号：优先使用forcedBatchNo，其次DTO自己的，最后生成一个新批次号（单个创建时）
        if (forcedBatchNo != null) {
            record.setBatchNo(forcedBatchNo);
        } else if (StringUtils.hasText(dto.getBatchNo())) {
            record.setBatchNo(dto.getBatchNo());
        } else {
            record.setBatchNo(generateBatchNo());
        }

        RecordRoleEnum recordRole = record.getRecordRole();

        // 保存
        return transactionRepo.save(record);
    }

    // ========== 校验辅助方法 ==========

    private void validateTransactionBusinessRules(CreateTransactionRecordDto dto, Account account) {
        // 金额与出入账方向一致性校验
        TransactionType transactionType = dto.getTransactionType();
        // 转账类型特殊校验
        if (transactionType == TransactionType.TRANSFER) {
            if (dto.getTargetAccountId() == null) {
                throw new BusinessException("转账类型必须指定目标账户ID(targetAccountId)");
            }
            if (dto.getTargetAccountId().equals(account.getId())) {
                throw new BusinessException("转账目标账户不能是源账户本身");
            }
            // 可选：检查目标账户是否存在，这里为了性能只做非空校验，实际可加载校验
        } else {
            if (dto.getTargetAccountId() != null) {
                throw new BusinessException("非转账类型不应提供目标账户ID");
            }
        }

        // 余额校验：如果提供了交易后余额，不能为负数
        if (dto.getBalance() != null && dto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("交易余额不能为负数");
        }

        // 费用校验：费用不能为负数
        if (dto.getFee() != null && dto.getFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("交易费用不能为负数");
        }

        // 账户类型与转账合理性（示例：支付宝账户不能转给信用卡等，可根据需要扩展）
        // ...
    }

    private String generateBatchNo() {
        return "BATCH_" + UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 16);
    }

    public TransactionDirectionTypeEnum inferTransactionDirection(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return TransactionDirectionTypeEnum.EXPENSE;
        }
        return TransactionDirectionTypeEnum.INCOME;
    }

    /**
     * 为批量创建生成统一的批次号（仅当列表中的所有DTO都没有指定batchNo时）
     */
    private String generateCommonBatchNo(List<CreateTransactionRecordDto> recordDtoList) {
        boolean allMissingBatchNo = recordDtoList.stream().noneMatch(dto -> StringUtils.hasText(dto.getBatchNo()));
        return allMissingBatchNo ? generateBatchNo() : null;
    }
}

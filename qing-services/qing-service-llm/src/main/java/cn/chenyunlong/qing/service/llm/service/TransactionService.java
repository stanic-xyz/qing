package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.transactions.CreateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.dto.transactions.UpdateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.event.TransactionChangeEvent;
import cn.chenyunlong.qing.service.llm.enums.*;
import cn.chenyunlong.qing.service.llm.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRecordRepository transactionRepo;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 创建单条流水
     */
    @Transactional(rollbackFor = Exception.class)
    public TransactionRecord create(@Valid CreateTransactionRecordDto dto) {
        TransactionRecord record = doCreate(dto, null);
        publishTransactionChange(record, TransactionChangeEvent.Action.CREATED);
        return record;
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
            TransactionRecord record = doCreate(dto, effectiveBatchNo);
            publishTransactionChange(record, TransactionChangeEvent.Action.CREATED);
            records.add(record);
        }
        return records;
    }

    /**
     * 按更新 DTO 的字段显式性执行部分更新。
     *
     * @param id  交易记录 ID
     * @param dto 更新 DTO
     * @return 更新后的交易记录
     */
    @Transactional(rollbackFor = Exception.class)
    public TransactionRecord update(Long id, @Valid UpdateTransactionRecordDto dto) {
        TransactionRecord record = getTransactionOrThrow(id);

        applyCategoryUpdate(record, dto);
        applyAmountUpdate(record, dto);
        applyCounterpartyUpdate(record, dto);
        applyMerchantUpdate(record, dto);
        applyTransactionTypeUpdate(record, dto);
        applyDirectionTypeUpdate(record, dto);
        validateManualWriteContract(record);

        record.setIsModified(true);
        TransactionRecord saved = transactionRepo.save(record);
        publishTransactionChange(saved, TransactionChangeEvent.Action.UPDATED);
        return saved;
    }

    /**
     * 核心创建逻辑
     *
     * @param dto           流水DTO
     * @param forcedBatchNo 强制使用的批次号（批量调用时统一传入，单个调用时传null）
     */
    private TransactionRecord doCreate(CreateTransactionRecordDto dto, String forcedBatchNo) {
        validateManualCreateTransferBoundary(dto);

        // 1. 基础关联实体校验与加载
        Long accountId = dto.getAccountId();
        if (accountId == null) {
            throw new BusinessException("账户ID不能为空");
        }
        Account account = null;
        account = getAccountOrThrow(accountId);
        Category category = resolveCategory(dto.getCategoryId(), dto.getCategoryName());

        // 2. 构建实体
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
        record.setTransactionType(dto.getTransactionType());
        record.setTransactionRecordType(dto.getTransactionRecordType());
        record.setDirectionType(dto.getDirectionType());

        // 对手方处理：优先使用ID加载，否则使用文本
        if (dto.getCounterpartyId() != null) {
            Counterparty cp = getCounterpartyOrThrow(dto.getCounterpartyId());
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

        // 3. 在持久化前统一校验方向、金额与分类契约
        validateManualWriteContract(record);

        // 保存
        return transactionRepo.save(record);
    }

    // ========== 校验辅助方法 ==========

    /**
     * 应用分类更新语义。
     * 未传保持不变，显式传 null 时清空分类。
     */
    private void applyCategoryUpdate(TransactionRecord record, UpdateTransactionRecordDto dto) {
        if (!dto.isCategoryIdSpecified() && !dto.isCategoryNameSpecified()) {
            return;
        }
        Category category = resolveCategory(dto.getCategoryId(), dto.getCategoryName());
        record.setCategory(category);
    }

    /**
     * 应用金额更新语义。
     * 金额字段允许缺省，但显式传入时不能为空，并同步刷新方向字段。
     */
    private void applyAmountUpdate(TransactionRecord record, UpdateTransactionRecordDto dto) {
        if (!dto.isAmountSpecified()) {
            return;
        }
        if (dto.getAmount() == null) {
            throw new BusinessException("交易金额不能为空");
        }
        record.setAmount(dto.getAmount());
    }

    /**
     * 应用对手方更新语义。
     * 支持保持不变、按 ID 关联、按文本覆盖和显式清空。
     */
    private void applyCounterpartyUpdate(TransactionRecord record, UpdateTransactionRecordDto dto) {
        if (!dto.isCounterpartyIdSpecified() && !dto.isCounterpartyStrSpecified()) {
            return;
        }
        if (dto.isCounterpartyIdSpecified() && dto.getCounterpartyId() != null) {
            Counterparty counterparty = getCounterpartyOrThrow(dto.getCounterpartyId());
            record.setCounterparty(counterparty);
            record.setCounterpartyStr(counterparty.getName());
            return;
        }
        if (dto.isCounterpartyStrSpecified()) {
            record.setCounterparty(null);
            record.setCounterpartyStr(normalizeNullableText(dto.getCounterpartyStr()));
            return;
        }
        record.setCounterparty(null);
        record.setCounterpartyStr(null);
    }

    /**
     * 应用商户更新语义。
     * 未传保持原值，显式传 null 或空白时清空。
     */
    private void applyMerchantUpdate(TransactionRecord record, UpdateTransactionRecordDto dto) {
        if (!dto.isMerchantSpecified()) {
            return;
        }
        record.setMerchant(normalizeNullableText(dto.getMerchant()));
    }

    /**
     * 应用业务交易类型更新语义。
     * 未传保持不变，显式传 null 时清空。
     */
    private void applyTransactionTypeUpdate(TransactionRecord record, UpdateTransactionRecordDto dto) {
        if (!dto.isTransactionTypeSpecified()) {
            return;
        }
        if (dto.getTransactionType() == TransactionType.TRANSFER) {
            throw unsupportedManualTransferException();
        }
        record.setTransactionType(dto.getTransactionType());
    }

    /**
     * 应用收支方向更新语义。
     * 未传保持不变，显式传 null 时拒绝清空。
     */
    private void applyDirectionTypeUpdate(TransactionRecord record, UpdateTransactionRecordDto dto) {
        if (!dto.isDirectionTypeSpecified()) {
            return;
        }
        if (dto.getDirectionType() == null) {
            throw new BusinessException("directionType不能为空");
        }
        record.setDirectionType(dto.getDirectionType());
    }

    /**
     * 统一校验手工交易写接口的金额、方向与分类契约。
     */
    private void validateManualWriteContract(TransactionRecord record) {
        if (record.getAmount() == null) {
            throw new BusinessException("交易金额不能为空");
        }
        if (record.getDirectionType() == null) {
            throw new BusinessException("directionType不能为空");
        }
        validateAmountDirectionConsistency(record.getAmount(), record.getDirectionType());
        validateBalanceAndFee(record);
        validateCategoryContract(record);
    }

    /**
     * 校验手工新增接口不再承载转账语义。
     */
    private void validateManualCreateTransferBoundary(CreateTransactionRecordDto dto) {
        if (dto.getTransactionType() == TransactionType.TRANSFER || dto.getTargetAccountId() != null) {
            throw unsupportedManualTransferException();
        }
    }

    /**
     * 校验余额与费用字段的非负约束。
     */
    private void validateBalanceAndFee(TransactionRecord record) {
        if (record.getBalance() != null && record.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("交易余额不能为负数");
        }
        if (record.getFee() != null && record.getFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("交易费用不能为负数");
        }
    }

    /**
     * 校验金额符号与显式方向是否一致。
     * 0 金额沿用历史兼容行为，不额外阻断。
     */
    private void validateAmountDirectionConsistency(BigDecimal amount,
                                                    TransactionDirectionTypeEnum directionType) {
        if (amount == null || directionType == null) {
            return;
        }
        int amountSign = amount.compareTo(BigDecimal.ZERO);
        if (amountSign == 0) {
            return;
        }
        if (directionType == TransactionDirectionTypeEnum.INCOME && amountSign < 0) {
            throw new BusinessException("amount与directionType不匹配，收入使用正金额，支出使用负金额");
        }
        if (directionType == TransactionDirectionTypeEnum.EXPENSE && amountSign > 0) {
            throw new BusinessException("amount与directionType不匹配，收入使用正金额，支出使用负金额");
        }
    }

    /**
     * 校验分类类型与当前交易方向的兼容性。
     */
    private void validateCategoryContract(TransactionRecord record) {
        Category category = record.getCategory();
        if (category == null || !StringUtils.hasText(category.getType())) {
            return;
        }
        String categoryType = category.getType().trim().toUpperCase();
        switch (categoryType) {
            case "INCOME" -> {
                if (record.getDirectionType() != TransactionDirectionTypeEnum.INCOME) {
                    throw new BusinessException("分类方向与交易方向不匹配，分类[" + category.getName() + "]要求收入方向");
                }
            }
            case "EXPENSE" -> {
                if (record.getDirectionType() != TransactionDirectionTypeEnum.EXPENSE) {
                    throw new BusinessException("分类方向与交易方向不匹配，分类[" + category.getName() + "]要求支出方向");
                }
            }
            default -> {
                // 历史脏数据或未规范分类类型时不额外阻断现有流程。
            }
        }
    }

    /**
     * 按 ID 加载交易记录，不存在时抛出资源不存在异常。
     *
     * @param transactionId 交易记录 ID
     * @return 交易记录实体
     */
    private TransactionRecord getTransactionOrThrow(Long transactionId) {
        return transactionRepo.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("记录不存在，id=" + transactionId));
    }

    /**
     * 按 ID 加载账户，不存在时抛出资源不存在异常。
     *
     * @param accountId 账户 ID
     * @return 账户实体
     */
    private Account getAccountOrThrow(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("账户不存在，accountId=" + accountId));
    }

    /**
     * 按 ID 加载交易分类，不存在时抛出资源不存在异常。
     *
     * @param categoryId 分类 ID
     * @return 分类实体
     */
    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("交易类别不存在，categoryId=" + categoryId));
    }

    /**
     * 按 ID 加载对手方，不存在时抛出资源不存在异常。
     *
     * @param counterpartyId 对手方 ID
     * @return 对手方实体
     */
    private Counterparty getCounterpartyOrThrow(Long counterpartyId) {
        return counterpartyRepository.findById(counterpartyId)
                .orElseThrow(() -> new NotFoundException("对手方不存在，counterpartyId=" + counterpartyId));
    }

    private String generateBatchNo() {
        return "BATCH_" + UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 16);
    }

    /**
     * 为批量创建生成统一的批次号（仅当列表中的所有DTO都没有指定batchNo时）
     */
    private String generateCommonBatchNo(List<CreateTransactionRecordDto> recordDtoList) {
        boolean allMissingBatchNo = recordDtoList.stream().noneMatch(dto -> StringUtils.hasText(dto.getBatchNo()));
        return allMissingBatchNo ? generateBatchNo() : null;
    }

    /**
     * 归一化可空文本字段，空白字符串按清空处理。
     */
    private String normalizeNullableText(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    /**
     * 生成当前手工交易接口不支持转账语义时的统一异常。
     */
    private BusinessException unsupportedManualTransferException() {
        return new BusinessException("当前手工交易接口不支持TRANSFER语义，请使用独立转账接口");
    }

    /**
     * 按分类 ID 或分类名称解析分类实体。
     * 同时传入两者时要求名称与 ID 指向的分类一致；两者都为空时返回 null。
     */
    private Category resolveCategory(Long categoryId, String categoryName) {
        String normalizedCategoryName = normalizeNullableText(categoryName);
        if (categoryId == null && normalizedCategoryName == null) {
            return null;
        }
        if (categoryId != null) {
            Category category = getCategoryOrThrow(categoryId);
            if (normalizedCategoryName != null && !normalizedCategoryName.equals(category.getName())) {
                throw new BusinessException("categoryId与categoryName不匹配");
            }
            return category;
        }
        return getCategoryByNameOrThrow(normalizedCategoryName);
    }

    /**
     * 按分类名称加载分类，不存在时抛出资源不存在异常。
     */
    private Category getCategoryByNameOrThrow(String categoryName) {
        Category category = categoryRepository.findByNameAndIsDeletedFalse(categoryName);
        if (category == null) {
            throw new NotFoundException("交易类别不存在，categoryName=" + categoryName);
        }
        return category;
    }

    /**
     * 发布交易写操作变更事件。
     */
    private void publishTransactionChange(TransactionRecord record, TransactionChangeEvent.Action action) {
        eventPublisher.publishEvent(new TransactionChangeEvent(record, record.getId(), action));
    }
}

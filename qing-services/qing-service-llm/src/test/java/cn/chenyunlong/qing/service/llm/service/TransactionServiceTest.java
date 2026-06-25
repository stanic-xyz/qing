package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.transactions.CreateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.dto.transactions.UpdateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.event.TransactionChangeEvent;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRecordRepository transactionRepo;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        lenient().when(transactionRepo.save(any(TransactionRecord.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("传入分类ID时应正确关联分类实体")
    void createShouldSaveCategoryWhenCategoryIdProvided() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCategoryId(20L);

        Account account = mockExistingAccount();
        Category category = mockCategory(20L, "工资", "INCOME");

        TransactionRecord record = transactionService.create(dto);

        assertSame(account, record.getAccount());
        assertSame(category, record.getCategory());
        assertEquals("主账户", record.getAccountName());
    }

    /**
     * 验证创建时仅传分类名称也能解析并写入分类关联。
     */
    @Test
    @DisplayName("创建时传入categoryName应正确关联分类实体")
    void createShouldSaveCategoryWhenCategoryNameProvided() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCategoryName("工资");

        mockExistingAccount();
        Category category = mockCategory(20L, "工资", "INCOME");

        TransactionRecord record = transactionService.create(dto);

        assertSame(category, record.getCategory());
    }

    @Test
    @DisplayName("仅传最小必要字段时应创建成功并发布创建事件")
    void createShouldSucceedWithMinimumRequiredFieldsAndPublishChangeEvent() {
        CreateTransactionRecordDto dto = baseDto();
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertEquals(new BigDecimal("88.80"), record.getAmount());
        assertEquals(10L, record.getAccount().getId());
        assertEquals(TransactionDirectionTypeEnum.INCOME, record.getDirectionType());

        ArgumentCaptor<TransactionChangeEvent> captor = ArgumentCaptor.forClass(TransactionChangeEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertEquals(record.getId(), captor.getValue().getTransactionId());
        assertEquals(TransactionChangeEvent.Action.CREATED, captor.getValue().getAction());
    }

    @Test
    @DisplayName("未传入分类ID时应保存成功且分类为空")
    void createShouldSaveSuccessfullyWhenCategoryIdMissing() {
        CreateTransactionRecordDto dto = baseDto();
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertNull(record.getCategory());
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("商户为空时应保存成功")
    void createShouldSaveSuccessfullyWhenMerchantMissing() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setMerchant(null);
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertNull(record.getMerchant());
        assertEquals(new BigDecimal("88.80"), record.getAmount());
    }

    @Test
    @DisplayName("未传入对手方ID和文本时应保存成功")
    void createShouldSaveSuccessfullyWhenCounterpartyInfoMissing() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCounterpartyId(null);
        dto.setCounterpartyStr(null);
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertNull(record.getCounterparty());
        assertNull(record.getCounterpartyStr());
        assertEquals(TransactionDirectionTypeEnum.INCOME, record.getDirectionType());
    }

    /**
     * 验证创建时传入有效对手方 ID 会优先关联实体并回填冗余名称。
     */
    @Test
    @DisplayName("创建时传入有效对手方ID应关联实体并覆盖文本")
    void createShouldAssociateCounterpartyWhenCounterpartyIdProvided() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCounterpartyId(88L);
        dto.setCounterpartyStr("待覆盖文本");
        mockExistingAccount();
        Counterparty counterparty = mockExistingCounterparty(88L, "测试对手方");

        TransactionRecord record = transactionService.create(dto);

        assertSame(counterparty, record.getCounterparty());
        assertEquals("测试对手方", record.getCounterpartyStr());
    }

    /**
     * 验证创建时传入不存在的对手方 ID 会被拒绝。
     */
    @Test
    @DisplayName("创建时传入不存在的对手方ID应抛出业务异常")
    void createShouldThrowWhenCounterpartyIdDoesNotExist() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCounterpartyId(999L);
        mockExistingAccount();
        when(counterpartyRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("对手方不存在"));
    }

    /**
     * 验证手工新增接口显式传入转账类型时会被拒绝。
     */
    @Test
    @DisplayName("创建时显式传入TRANSFER类型应抛出业务异常")
    void createShouldThrowWhenTransferTransactionTypeProvided() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setTransactionType(TransactionType.TRANSFER);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("不支持TRANSFER语义"));
    }

    /**
     * 验证手工新增接口显式传入目标账户时会被拒绝。
     */
    @Test
    @DisplayName("创建时显式传入目标账户ID应抛出业务异常")
    void createShouldThrowWhenTargetAccountProvided() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setTargetAccountId(20L);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("不支持TRANSFER语义"));
    }

    /**
     * 验证手工新增接口不会再用目标账户参与旧转账语义。
     */
    @Test
    @DisplayName("创建时同时传入非TRANSFER类型和目标账户ID仍应抛出统一业务异常")
    void createShouldThrowUnifiedExceptionWhenTargetAccountProvidedWithNonTransferType() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setAmount(new BigDecimal("-20.22"));
        dto.setTransactionType(TransactionType.EXPENSE);
        dto.setDirectionType(TransactionDirectionTypeEnum.EXPENSE);
        dto.setTargetAccountId(20L);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("不支持TRANSFER语义"));
    }

    /**
     * 验证创建时负余额会触发业务校验失败。
     */
    @Test
    @DisplayName("创建时余额为负数应抛出业务异常")
    void createShouldThrowWhenBalanceIsNegative() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setBalance(new BigDecimal("-0.01"));
        mockExistingAccount();

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("交易余额不能为负数"));
    }

    /**
     * 验证创建时负费用会触发业务校验失败。
     */
    @Test
    @DisplayName("创建时费用为负数应抛出业务异常")
    void createShouldThrowWhenFeeIsNegative() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setFee(new BigDecimal("-0.01"));
        mockExistingAccount();

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("交易费用不能为负数"));
    }

    /**
     * 验证负金额支出在符号与方向一致时可以成功保存。
     */
    @Test
    @DisplayName("创建时负金额支出且符号匹配应保存成功")
    void createShouldPersistExpenseDirectionWhenAmountIsNegative() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setAmount(new BigDecimal("-20.22"));
        dto.setTransactionType(TransactionType.EXPENSE);
        dto.setDirectionType(TransactionDirectionTypeEnum.EXPENSE);
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertEquals(new BigDecimal("-20.22"), record.getAmount());
        assertEquals(TransactionDirectionTypeEnum.EXPENSE, record.getDirectionType());
        assertEquals(TransactionType.EXPENSE, record.getTransactionType());
    }

    /**
     * 验证创建时金额符号与方向不一致会被服务层显式拒绝。
     */
    @Test
    @DisplayName("创建时金额符号与directionType不匹配应抛出业务异常")
    void createShouldThrowWhenAmountSignDoesNotMatchDirectionType() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setAmount(new BigDecimal("-20.22"));
        mockExistingAccount();

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("amount与directionType不匹配"));
    }

    /**
     * 验证创建时不再使用 transactionType 参与方向合法性校验。
     */
    @Test
    @DisplayName("创建时transactionType与directionType不匹配但金额符号正确应保存成功")
    void createShouldIgnoreTransactionTypeDirectionMismatchWhenAmountSignMatches() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setTransactionType(TransactionType.EXPENSE);
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertEquals(TransactionType.EXPENSE, record.getTransactionType());
        assertEquals(TransactionDirectionTypeEnum.INCOME, record.getDirectionType());
        assertEquals(new BigDecimal("88.80"), record.getAmount());
    }

    @Test
    @DisplayName("传入不存在的分类ID时应抛出业务异常")
    void createShouldThrowWhenCategoryIdDoesNotExist() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCategoryId(999L);
        mockExistingAccount();
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("交易类别不存在"));
    }

    /**
     * 验证创建时分类方向不匹配会被拒绝。
     */
    @Test
    @DisplayName("创建时分类方向与交易方向不匹配应抛出业务异常")
    void createShouldThrowWhenCategoryDirectionDoesNotMatch() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCategoryId(30L);
        mockExistingAccount();
        mockCategory(30L, "餐饮", "EXPENSE");

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("分类方向与交易方向不匹配"));
    }

    /**
     * 验证创建时按分类名称解析后也会执行方向兼容校验。
     */
    @Test
    @DisplayName("创建时按categoryName命中的分类方向不匹配应抛出业务异常")
    void createShouldThrowWhenCategoryNameDirectionDoesNotMatch() {
        CreateTransactionRecordDto dto = baseDto();
        dto.setCategoryName("餐饮");
        mockExistingAccount();
        mockCategory(30L, "餐饮", "EXPENSE");

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.create(dto));

        assertTrue(exception.getMessage().contains("分类方向与交易方向不匹配"));
    }

    @Test
    @DisplayName("未传入状态相关字段和批次号时应使用默认值")
    void createShouldApplyDefaultValuesAndGenerateBatchNo() {
        CreateTransactionRecordDto dto = baseDto();
        mockExistingAccount();

        TransactionRecord record = transactionService.create(dto);

        assertEquals(TransactionStatusEnum.SUCCESS, record.getStatus());
        assertEquals(ReconciliationStatusEnum.PENDING, record.getReconciliationStatus());
        assertFalse(record.getConfirmed());
        assertEquals(MatchStatusEnum.ORIGINAL, record.getMatchStatus());
        assertEquals(RecordRoleEnum.PRIMARY, record.getRecordRole());
        assertNotNull(record.getBatchNo());
        assertTrue(record.getBatchNo().startsWith("BATCH_"));
    }

    @Test
    @DisplayName("更新时未传分类字段应保持原分类不变")
    void updateShouldKeepCategoryWhenCategoryNotSpecified() {
        TransactionRecord record = existingRecord();
        Category category = new Category();
        category.setId(30L);
        category.setName("工资");
        category.setType("INCOME");
        record.setCategory(category);
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        TransactionRecord updated = transactionService.update(1L, dto);

        assertSame(category, updated.getCategory());
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("更新不存在的记录时应抛出资源不存在异常")
    void updateShouldThrowWhenTransactionDoesNotExist() {
        when(transactionRepo.findById(404L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> transactionService.update(404L, new UpdateTransactionRecordDto()));

        assertTrue(exception.getMessage().contains("记录不存在"));
    }

    @Test
    @DisplayName("更新时显式传空分类应清空分类")
    void updateShouldClearCategoryWhenCategoryExplicitlyNull() {
        TransactionRecord record = existingRecord();
        Category category = new Category();
        category.setId(30L);
        record.setCategory(category);
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setCategoryId(null);

        TransactionRecord updated = transactionService.update(1L, dto);

        assertNull(updated.getCategory());
        assertTrue(updated.getIsModified());
    }

    /**
     * 验证更新时仅传分类名称也能解析并写入分类关联。
     */
    @Test
    @DisplayName("更新时传入categoryName应正确关联分类实体")
    void updateShouldSaveCategoryWhenCategoryNameProvided() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));
        Category category = mockCategory(30L, "工资", "INCOME");

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setCategoryName("工资");

        TransactionRecord updated = transactionService.update(1L, dto);

        assertSame(category, updated.getCategory());
    }

    @Test
    @DisplayName("更新时显式传空商户和对手方应清空对应字段")
    void updateShouldClearMerchantAndCounterpartyWhenExplicitlyNull() {
        TransactionRecord record = existingRecord();
        Counterparty counterparty = new Counterparty();
        counterparty.setId(50L);
        counterparty.setName("旧对手方");
        record.setCounterparty(counterparty);
        record.setCounterpartyStr("旧对手方");
        record.setMerchant("旧商户");
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setMerchant(null);
        dto.setCounterpartyStr(null);

        TransactionRecord updated = transactionService.update(1L, dto);

        assertNull(updated.getMerchant());
        assertNull(updated.getCounterparty());
        assertNull(updated.getCounterpartyStr());
    }

    /**
     * 验证更新时传入有效对手方 ID 会关联实体并回填冗余名称。
     */
    @Test
    @DisplayName("更新时传入有效对手方ID应关联实体并覆盖文本")
    void updateShouldAssociateCounterpartyWhenCounterpartyIdProvided() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));
        Counterparty counterparty = mockExistingCounterparty(66L, "新对手方");

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setCounterpartyId(66L);
        dto.setCounterpartyStr("待覆盖文本");

        TransactionRecord updated = transactionService.update(1L, dto);

        assertSame(counterparty, updated.getCounterparty());
        assertEquals("新对手方", updated.getCounterpartyStr());
    }

    /**
     * 验证更新时传入不存在的对手方 ID 会被拒绝。
     */
    @Test
    @DisplayName("更新时传入不存在的对手方ID应抛出业务异常")
    void updateShouldThrowWhenCounterpartyIdDoesNotExist() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));
        when(counterpartyRepository.findById(999L)).thenReturn(Optional.empty());

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setCounterpartyId(999L);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("对手方不存在"));
    }

    /**
     * 验证更新时显式切换为转账类型会被拒绝。
     */
    @Test
    @DisplayName("更新时显式传入TRANSFER类型应抛出业务异常")
    void updateShouldThrowWhenTransactionTypeExplicitlySetToTransfer() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setTransactionType(TransactionType.TRANSFER);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("不支持TRANSFER语义"));
    }

    /**
     * 验证更新时显式传空金额会触发业务异常。
     */
    @Test
    @DisplayName("更新时显式传空金额应抛出业务异常")
    void updateShouldThrowWhenAmountExplicitlyNull() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setAmount(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("交易金额不能为空"));
    }

    /**
     * 验证更新时显式传空方向会触发业务异常。
     */
    @Test
    @DisplayName("更新时显式传空directionType应抛出业务异常")
    void updateShouldThrowWhenDirectionTypeExplicitlyNull() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setDirectionType(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("directionType不能为空"));
    }

    /**
     * 验证更新时金额符号与最终方向不一致会被服务层显式拒绝。
     */
    @Test
    @DisplayName("更新时金额符号与directionType不匹配应抛出业务异常")
    void updateShouldThrowWhenAmountSignDoesNotMatchDirectionType() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setAmount(new BigDecimal("-18.80"));

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("amount与directionType不匹配"));
    }

    /**
     * 验证更新时传入空白商户和空白对手方文本会按清空语义处理。
     */
    @Test
    @DisplayName("更新时传入空白商户和空白对手方文本应清空对应字段")
    void updateShouldClearMerchantAndCounterpartyTextWhenBlank() {
        TransactionRecord record = existingRecord();
        Counterparty counterparty = new Counterparty();
        counterparty.setId(50L);
        counterparty.setName("旧对手方");
        record.setCounterparty(counterparty);
        record.setCounterpartyStr("旧对手方");
        record.setMerchant("旧商户");
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setMerchant("   ");
        dto.setCounterpartyStr("  ");

        TransactionRecord updated = transactionService.update(1L, dto);

        assertNull(updated.getMerchant());
        assertNull(updated.getCounterparty());
        assertNull(updated.getCounterpartyStr());
    }

    @Test
    @DisplayName("更新时只改金额应保持原方向不变")
    void updateShouldKeepDirectionWhenOnlyAmountChanges() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setAmount(new BigDecimal("18.80"));

        TransactionRecord updated = transactionService.update(1L, dto);

        assertEquals(new BigDecimal("18.80"), updated.getAmount());
        assertEquals(TransactionDirectionTypeEnum.INCOME, updated.getDirectionType());
    }

    /**
     * 验证更新时不再使用 transactionType 参与方向合法性校验。
     */
    @Test
    @DisplayName("更新时transactionType与directionType不匹配但金额符号正确应保存成功")
    void updateShouldIgnoreTransactionTypeDirectionMismatchWhenAmountSignMatches() {
        TransactionRecord record = existingRecord();
        record.setTransactionType(TransactionType.INCOME);
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setTransactionType(TransactionType.EXPENSE);

        TransactionRecord updated = transactionService.update(1L, dto);

        assertEquals(TransactionType.EXPENSE, updated.getTransactionType());
        assertEquals(TransactionDirectionTypeEnum.INCOME, updated.getDirectionType());
        assertEquals(new BigDecimal("66.00"), updated.getAmount());
    }

    /**
     * 验证更新时可显式同步调整业务类型与收支方向。
     */
    @Test
    @DisplayName("更新成功后应发布更新事件并按显式字段更新语义保存")
    void updateShouldPublishChangeEventAfterUpdate() {
        TransactionRecord record = existingRecord();
        record.setTransactionType(TransactionType.INCOME);
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setAmount(new BigDecimal("-18.80"));
        dto.setTransactionType(TransactionType.EXPENSE);
        dto.setDirectionType(TransactionDirectionTypeEnum.EXPENSE);

        TransactionRecord updated = transactionService.update(1L, dto);

        assertEquals(new BigDecimal("-18.80"), updated.getAmount());
        assertEquals(TransactionDirectionTypeEnum.EXPENSE, updated.getDirectionType());
        assertEquals(TransactionType.EXPENSE, updated.getTransactionType());
        assertTrue(updated.getIsModified());

        ArgumentCaptor<TransactionChangeEvent> captor = ArgumentCaptor.forClass(TransactionChangeEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertEquals(1L, captor.getValue().getTransactionId());
        assertEquals(TransactionChangeEvent.Action.UPDATED, captor.getValue().getAction());
    }

    /**
     * 验证更新时切换到匹配方向的分类会成功。
     */
    @Test
    @DisplayName("更新时同时调整方向与分类且二者匹配应成功")
    void updateShouldAllowCategorySwitchWhenDirectionMatches() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));
        Category category = mockCategory(40L, "餐饮", "EXPENSE");

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setAmount(new BigDecimal("-66.00"));
        dto.setDirectionType(TransactionDirectionTypeEnum.EXPENSE);
        dto.setTransactionType(TransactionType.EXPENSE);
        dto.setCategoryId(40L);

        TransactionRecord updated = transactionService.update(1L, dto);

        assertSame(category, updated.getCategory());
        assertEquals(TransactionDirectionTypeEnum.EXPENSE, updated.getDirectionType());
        assertEquals(TransactionType.EXPENSE, updated.getTransactionType());
    }

    /**
     * 验证更新时分类方向与最终交易方向不匹配会被拒绝。
     */
    @Test
    @DisplayName("更新时分类方向与交易方向不匹配应抛出业务异常")
    void updateShouldThrowWhenCategoryDirectionDoesNotMatch() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));
        mockCategory(41L, "餐饮", "EXPENSE");

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setCategoryId(41L);

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("分类方向与交易方向不匹配"));
    }

    /**
     * 验证更新时按分类名称解析后也会执行方向兼容校验。
     */
    @Test
    @DisplayName("更新时按categoryName命中的分类方向不匹配应抛出业务异常")
    void updateShouldThrowWhenCategoryNameDirectionDoesNotMatch() {
        TransactionRecord record = existingRecord();
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(record));
        mockCategory(41L, "餐饮", "EXPENSE");

        UpdateTransactionRecordDto dto = new UpdateTransactionRecordDto();
        dto.setCategoryName("餐饮");

        BusinessException exception = assertThrows(BusinessException.class, () -> transactionService.update(1L, dto));

        assertTrue(exception.getMessage().contains("分类方向与交易方向不匹配"));
    }

    /**
     * 构造满足最小创建条件的 DTO，便于聚焦单个测试变量。
     */
    private CreateTransactionRecordDto baseDto() {
        CreateTransactionRecordDto dto = new CreateTransactionRecordDto();
        dto.setAccountId(10L);
        dto.setTransactionTime(LocalDateTime.now().minusHours(1));
        dto.setAmount(new BigDecimal("88.80"));
        dto.setDirectionType(TransactionDirectionTypeEnum.INCOME);
        return dto;
    }

    /**
     * 构造一个已存在的交易记录，便于验证更新语义。
     */
    private TransactionRecord existingRecord() {
        TransactionRecord record = new TransactionRecord();
        record.setId(1L);
        record.setAmount(new BigDecimal("66.00"));
        record.setDirectionType(TransactionDirectionTypeEnum.INCOME);
        record.setMerchant("初始商户");
        record.setCounterpartyStr("初始对手方");
        record.setIsModified(false);
        return record;
    }

    /**
     * 模拟已存在的账户实体，复用交易创建所需的基础依赖。
     */
    private Account mockExistingAccount() {
        Account account = new Account();
        account.setId(10L);
        account.setAccountName("主账户");
        account.setAccountType(AccountType.DEBIT);
        when(accountRepository.findById(10L)).thenReturn(Optional.of(account));
        return account;
    }

    /**
     * 模拟已存在的对手方实体，便于覆盖创建与更新的关联场景。
     */
    private Counterparty mockExistingCounterparty(Long id, String name) {
        Counterparty counterparty = new Counterparty();
        counterparty.setId(id);
        counterparty.setName(name);
        when(counterpartyRepository.findById(id)).thenReturn(Optional.of(counterparty));
        return counterparty;
    }

    /**
     * 模拟已存在的分类实体，便于覆盖方向兼容性校验场景。
     */
    private Category mockCategory(Long id, String name, String type) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setType(type);
        lenient().when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        lenient().when(categoryRepository.findByNameAndIsDeletedFalse(name)).thenReturn(category);
        return category;
    }
}

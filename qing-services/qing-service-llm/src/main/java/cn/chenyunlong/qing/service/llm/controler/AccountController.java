package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.AccountPreviewResult;
import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.dto.account.AccountStatisticsDTO;
import cn.chenyunlong.qing.service.llm.dto.account.AccountUpdateDTO;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.event.AccountChangeEvent;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.service.AccountImportService;
import cn.chenyunlong.qing.service.llm.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepo;
    private final TransactionRecordRepository transactionRepo;
    private final AccountImportService accountImportService;
    private final ChannelRepository channelRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TransactionService transactionService;

    @GetMapping("/import/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        byte[] data = accountImportService.downloadTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "account_import_template.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    @PostMapping("/import/preview")
    public Result<AccountPreviewResult> previewImport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mode", defaultValue = "SKIP") ImportModeEnum mode) throws Exception {
        return Result.success(accountImportService.preview(file, mode));
    }

    @PostMapping("/import/execute")
    public Result<Integer> executeImport(
            @RequestBody List<AccountImportDTO> items,
            @RequestParam(value = "mode", defaultValue = "SKIP") ImportModeEnum mode) {
        int processed = accountImportService.executeImport(items, mode);
        return Result.success(processed);
    }

    @GetMapping
    public Result<List<AccountDTO>> getAccounts() {
        List<AccountDTO> accountDTOs = accountRepo.findAll().stream()
                .map(AccountDTO::of)
                .toList();
        return Result.success(accountDTOs);
    }

    @GetMapping("{id}")
    public Result<AccountDTO> getAccountById(@PathVariable Long id) {
        return accountRepo.findById(id).map(AccountDTO::of).map(Result::success).orElseThrow();
    }

    @PostMapping
    public Result<AccountDTO> createAccount(@RequestBody AccountUpdateDTO dto) {
        Account account = new Account();
        account.setAccountName(dto.getAccountName());
        account.setAccountType(dto.getAccountType());
        account.setBankName(dto.getBankName());
        account.setIcon(dto.getIcon());
        account.setRemark(dto.getRemark());
        account.setCardNumber(dto.getCardNumber());
        account.setInitialBalance(dto.getInitialBalance());
        account.setCurrentBalance(dto.getInitialBalance());
        account.setStatus(dto.getStatus() != null ? dto.getStatus() : AccountStatusEnum.ACTIVE);
        if (dto.getChannel() != null) {
            channelRepository.findByIdAndIsDeletedFalse(dto.getChannel()).ifPresent(account::setChannel);
        }

        return Result.success(AccountDTO.of(accountRepo.save(account)));
    }

    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/{accountId}")
    public Result<AccountDTO> updateAccount(@PathVariable("accountId") Long id, @RequestBody AccountUpdateDTO account) {
        Account existing = getAccountOrThrow(id, "Account not found");

        if (account.getChannel() != null) {
            Channel channel = getChannelOrThrow(account.getChannel(), "Channel not found");
            existing.setChannel(channel);
        } else {
            existing.setChannel(null);
        }

        existing.setAccountName(account.getAccountName());
        existing.setAccountType(account.getAccountType());
        existing.setBankName(account.getBankName());
        existing.setIcon(account.getIcon());
        existing.setRemark(account.getRemark());
        existing.setCardNumber(account.getCardNumber());
        existing.setInitialBalance(account.getInitialBalance());
        existing.setStatus(account.getStatus());
        Account saved = accountRepo.save(existing);
        applicationEventPublisher.publishEvent(new AccountChangeEvent(this, saved));
        return Result.success(AccountDTO.of(saved));
    }

    @GetMapping("/{id}/transaction-count")
    public Result<Long> getTransactionCount(@PathVariable("id") Long id) {
        Account account = getAccountOrThrow(id, "账户不存在");
        long count = transactionRepo.countByAccount(account);
        return Result.success(count);
    }

    @GetMapping("/{id}/statistics")
    public Result<AccountStatisticsDTO> getAccountStatistics(@PathVariable("id") Long id) {
        Account account = getAccountOrThrow(id, "账户不存在");

        List<TransactionRecord> transactions = transactionRepo.findAllByAccount(account);

        long incomeCount = 0;
        long expenseCount = 0;
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (TransactionRecord record : transactions) {
            if (Boolean.TRUE.equals(record.getIsDeleted())) {
                continue;
            }
            if (record.getDirectionType() == TransactionDirectionTypeEnum.INCOME ||
                    (record.getTransactionType() == TransactionType.INCOME)) {
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
        }

        AccountStatisticsDTO stats = new AccountStatisticsDTO();
        stats.setAccountId(account.getId());
        stats.setAccountName(account.getAccountName());
        stats.setTransactionCount((long) transactions.size());
        stats.setIncomeCount(incomeCount);
        stats.setExpenseCount(expenseCount);
        stats.setTotalIncome(totalIncome);
        stats.setTotalExpense(totalExpense);
        stats.setNetAmount(totalIncome.subtract(totalExpense));
        return Result.success(stats);
    }

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(
            @PathVariable("id") Long id,
            @RequestParam(value = "cascade", defaultValue = "false") boolean cascade) {
        Account account = getAccountOrThrow(id, "账户不存在");

        long transactionCount = transactionRepo.countByAccount(account);
        if (transactionCount > 0 && !cascade) {
            throw new BusinessException(String.format("该账户下有 %d 条关联流水，如需删除请同时删除关联流水", transactionCount));
        }

        if (cascade && transactionCount > 0) {
            transactionRepo.deleteAll(transactionRepo.findAllByAccount(account));
        }

        accountRepo.deleteById(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/calculate")
    public Result<Void> calculateBalance(@PathVariable("id") Long id) {

        // 计算余额 ，找到当前账号下面的所有记录，然后从历史的交易记录里面计算出当前账户的余额

        return Result.success();
    }

    @PostMapping("/{id}/calibrate")
    public Result<AccountDTO> calibrateBalance(@PathVariable("id") Long id, @RequestBody java.util.Map<String, BigDecimal> payload) {
        BigDecimal newBalance = payload.get("newBalance");
        if (newBalance == null) {
            throw new BusinessException("newBalance is required");
        }

        Account account = getAccountOrThrow(id, "Account not found");

        BigDecimal currentBalance = account.getCurrentBalance() != null ? account.getCurrentBalance() : BigDecimal.ZERO;
        BigDecimal diff = newBalance.subtract(currentBalance);

        if (diff.compareTo(BigDecimal.ZERO) != 0) {
            TransactionRecord record = new TransactionRecord();
            record.setAccount(account);
            record.setAccountName(account.getAccountName());
            record.setAccountType(account.getAccountType());
            record.setTransactionTime(LocalDateTime.now());
            record.setTransactionType(diff.compareTo(BigDecimal.ZERO) > 0 ? TransactionType.INCOME : TransactionType.EXPENSE);
            record.setAmount(diff.abs());
            record.setDetail("手动余额校准");
            record.setStatus(TransactionStatusEnum.SUCCESS);
            record.setConfirmed(true);
            record.setReconciliationStatus(ReconciliationStatusEnum.MATCHED);
            record.setIsImported(true);
            record.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.MANUAL_EDITED);

            transactionRepo.save(record);

            account.setCurrentBalance(newBalance);
            accountRepo.save(account);
        }

        return Result.success(AccountDTO.of(account));
    }

    /**
     * 按账户 ID 加载账户，不存在时抛出资源不存在异常。
     *
     * @param accountId 账户 ID
     * @param message 异常提示
     * @return 账户实体
     */
    private Account getAccountOrThrow(Long accountId, String message) {
        return accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException(message));
    }

    /**
     * 按渠道 ID 加载渠道，不存在时抛出资源不存在异常。
     *
     * @param channelId 渠道 ID
     * @param message 异常提示
     * @return 渠道实体
     */
    private Channel getChannelOrThrow(Long channelId, String message) {
        return channelRepository.findByIdAndIsDeletedFalse(channelId)
                .orElseThrow(() -> new NotFoundException(message));
    }
}

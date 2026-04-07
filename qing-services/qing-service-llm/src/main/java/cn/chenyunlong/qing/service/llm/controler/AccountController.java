package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.AccountPreviewResult;
import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.dto.account.AccountUpdateDTO;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.event.AccountChangeEvent;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.service.AccountImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/finance/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepo;
    private final TransactionRecordRepository transactionRepo;
    private final AccountImportService accountImportService;
    private final ChannelRepository channelRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/import/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            byte[] data = accountImportService.downloadTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "account_import_template.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(data);
        } catch (Exception e) {
            log.error("Failed to generate template", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/import/preview")
    public Result<AccountPreviewResult> previewImport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "mode", defaultValue = "SKIP") ImportModeEnum mode) {
        try {
            return Result.success(accountImportService.preview(file, mode));
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("Failed to preview import", e);
            return Result.error(500, "文件解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/import/execute")
    public Result<Integer> executeImport(
            @RequestBody List<AccountImportDTO> items,
            @RequestParam(value = "mode", defaultValue = "SKIP") ImportModeEnum mode) {
        try {
            int processed = accountImportService.executeImport(items, mode);
            return Result.success(processed);
        } catch (Exception e) {
            log.error("Failed to execute import", e);
            return Result.error(500, "导入失败: " + e.getMessage());
        }
    }

    @GetMapping
    public Result<List<AccountDTO>> getAccounts() {
        List<AccountDTO> accountDTOs = accountRepo.findAll().stream()
                .map(AccountDTO::of)
                .toList();
        return Result.success(accountDTOs);
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
    @PutMapping("/{id}")
    public Result<AccountDTO> updateAccount(@PathVariable("id") Long id, @RequestBody AccountUpdateDTO account) {
        Account existing = accountRepo.findById(id).orElse(null);
        if (existing == null) return Result.error(404, "Account not found");
        if (account.getChannel() != null) {
            Channel channel = channelRepository.findByIdAndIsDeletedFalse(account.getChannel()).orElseThrow();
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

    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(@PathVariable("id") Long id) {
        accountRepo.deleteById(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/calibrate")
    public Result<AccountDTO> calibrateBalance(@PathVariable("id") Long id, @RequestBody java.util.Map<String, BigDecimal> payload) {
        BigDecimal newBalance = payload.get("newBalance");
        if (newBalance == null) {
            return Result.error(400, "newBalance is required");
        }

        Account account = accountRepo.findById(id).orElse(null);
        if (account == null) {
            return Result.error(404, "Account not found");
        }

        BigDecimal currentBalance = account.getCurrentBalance() != null ? account.getCurrentBalance() : BigDecimal.ZERO;
        BigDecimal diff = newBalance.subtract(currentBalance);

        if (diff.compareTo(BigDecimal.ZERO) != 0) {
            TransactionRecord record = new TransactionRecord();
            record.setAccount(account);
            record.setAccountName(account.getAccountName());
            record.setAccountType(account.getAccountType());
            record.setChannel(account.getChannel());
            record.setTransactionTime(LocalDateTime.now());
            record.setType(diff.compareTo(BigDecimal.ZERO) > 0 ? TrasactionType.INCOME : TrasactionType.EXPENSE);
            record.setAmount(diff.abs());
            record.setSubCategory("余额平账");
            record.setRemark("手动余额校准");
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
}

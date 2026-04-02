package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.AccountImportDTO;
import cn.chenyunlong.qing.service.llm.dto.AccountPreviewResult;
import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.service.AccountImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRecordRepository transactionRepo;

    @Autowired
    private AccountImportService accountImportService;

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
    public Result<List<Account>> getAccounts() {
        return Result.success(accountRepo.findAll());
    }

    @PostMapping
    public Result<Account> createAccount(@RequestBody Account account) {
        if (account.getStatus() == null) {
            account.setStatus("ACTIVE");
        }
        return Result.success(accountRepo.save(account));
    }

    @PutMapping("/{id}")
    public Result<Account> updateAccount(@PathVariable("id") Long id, @RequestBody Account account) {
        Account existing = accountRepo.findById(id).orElse(null);
        if (existing == null) return Result.error(404, "Account not found");

        existing.setAccountName(account.getAccountName());
        existing.setAccountType(account.getAccountType());
        existing.setBankName(account.getBankName());
        existing.setChannel(account.getChannel());
        existing.setIcon(account.getIcon());
        existing.setRemark(account.getRemark());
        existing.setCardNumber(account.getCardNumber());
        existing.setInitialBalance(account.getInitialBalance());
        existing.setStatus(account.getStatus());

        return Result.success(accountRepo.save(existing));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(@PathVariable("id") Long id) {
        accountRepo.deleteById(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/calibrate")
    public Result<Account> calibrateBalance(@PathVariable("id") Long id, @RequestBody java.util.Map<String, BigDecimal> payload) {
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
            record.setType(diff.compareTo(BigDecimal.ZERO) > 0 ? "INCOME" : "EXPENSE");
            record.setAmount(diff.abs());
            record.setCounterparty("系统平账");
            record.setCategory("系统");
            record.setSubCategory("余额平账");
            record.setRemark("手动余额校准");
            record.setStatus("SUCCESS");
            record.setConfirmed(true);
            record.setReconciliationStatus("SUCCESS");
            record.setIsImported(true);
            record.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.MANUAL_EDITED);

            transactionRepo.save(record);

            account.setCurrentBalance(newBalance);
            accountRepo.save(account);
        }

        return Result.success(account);
    }
}

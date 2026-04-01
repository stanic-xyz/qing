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

@RestController
@RequestMapping("/api/finance/accounts")
@Slf4j
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

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
}

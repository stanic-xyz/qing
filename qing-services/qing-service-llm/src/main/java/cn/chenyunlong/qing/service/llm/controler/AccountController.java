package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/accounts")
@Slf4j
public class AccountController {

    @Autowired
    private AccountRepository accountRepo;

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
    public Result<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account existing = accountRepo.findById(id).orElse(null);
        if (existing == null) return Result.error(404, "Account not found");
        
        existing.setAccountName(account.getAccountName());
        existing.setAccountType(account.getAccountType());
        existing.setBankName(account.getBankName());
        existing.setCardNumber(account.getCardNumber());
        existing.setInitialBalance(account.getInitialBalance());
        existing.setStatus(account.getStatus());
        
        return Result.success(accountRepo.save(existing));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteAccount(@PathVariable Long id) {
        accountRepo.deleteById(id);
        return Result.success(null);
    }
}

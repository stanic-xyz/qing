package cn.chenyunlong.qing.service.llm.event.handler;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.event.AccountChangeEvent;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AccountChangeEventHandler {

    private final TransactionRecordRepository transactionRecordRepository;
    private final AccountRepository accountRepository;

    @EventListener(value = AccountChangeEvent.class)
    public void handleAccountChange(AccountChangeEvent event) {
        System.out.println("AccountChangeEvent");
        Optional<Account> accountOptional = accountRepository.findById(event.getAccountId());
        accountOptional.ifPresent(account -> {
            List<TransactionRecord> allByAccount = transactionRecordRepository.findAllByAccount(account);
            allByAccount.forEach(t -> t.setChannel(account.getChannel()));
            transactionRecordRepository.saveAll(allByAccount);
        });
    }
}

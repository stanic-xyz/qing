package cn.chenyunlong.qing.service.llm.config;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.ChannelAccountRel;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelAccountRelRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataMigrationRunner implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final ChannelRepository channelRepository;
    private final ChannelAccountRelRepository channelAccountRelRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Checking and migrating channel and account relationships...");

        // 1. Get all unique channels from Accounts
        List<Account> allAccounts = accountRepository.findAll();
        Set<String> accountChannels = allAccounts.stream()
                .map(Account::getChannel)
                .filter(c -> c != null && !c.isEmpty())
                .collect(Collectors.toSet());

        // 2. Get all unique channels from TransactionRecords
        List<TransactionRecord> allRecords = transactionRecordRepository.findAll();
        Set<String> recordChannels = allRecords.stream()
                .map(TransactionRecord::getChannel)
                .filter(c -> c != null && !c.isEmpty())
                .collect(Collectors.toSet());

        accountChannels.addAll(recordChannels);

        // 3. Create Channel entities if they don't exist
        for (String channelCode : accountChannels) {
            Channel channel = channelRepository.findByCode(channelCode).orElse(null);
            if (channel == null) {
                channel = new Channel();
                channel.setCode(channelCode);
                channel.setName(channelCode); // Will be updated later via frontend or manual
                channel.setStatus("EFFECTIVE");
                channel.setIsEnabled(true);
                channel.setCreatedBy("system_migration");
                channelRepository.save(channel);
                log.info("Migrated new Channel: {}", channelCode);
            }
        }

        // 4. Create ChannelAccountRel for existing accounts
        for (Account account : allAccounts) {
            if (account.getChannel() != null && !account.getChannel().isEmpty()) {
                Channel channel = channelRepository.findByCode(account.getChannel()).orElse(null);
                if (channel != null) {
                    boolean relExists = channelAccountRelRepository
                            .findByChannelIdAndAccountIdAndIsDeletedFalse(channel.getId(), account.getId())
                            .isPresent();
                    
                    if (!relExists) {
                        ChannelAccountRel rel = new ChannelAccountRel();
                        rel.setChannelId(channel.getId());
                        rel.setAccountId(account.getId());
                        rel.setStatus("EFFECTIVE");
                        rel.setCreatedBy("system_migration");
                        channelAccountRelRepository.save(rel);
                        log.info("Migrated relation: Channel {} <-> Account {}", channel.getCode(), account.getAccountName());
                    }
                }
            }
        }
        
        log.info("Data migration completed.");
    }
}

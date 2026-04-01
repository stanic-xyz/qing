package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.ChannelAccountRel;
import cn.chenyunlong.qing.service.llm.enums.ApprovalState;
import cn.chenyunlong.qing.service.llm.event.RelChangeEvent;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelAccountRelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelAccountRelService {

    private final ChannelAccountRelRepository relRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Cacheable(value = "channelAccounts", key = "#p0")
    public List<Account> getEffectiveAccountsByChannelId(Long channelId) {
        List<ChannelAccountRel> rels = relRepository.findByChannelIdAndIsDeletedFalse(channelId).stream()
                .filter(r -> ApprovalState.EFFECTIVE.name().equals(r.getStatus()))
                .toList();

        List<Long> accountIds = rels.stream().map(ChannelAccountRel::getAccountId).collect(Collectors.toList());
        return accountRepository.findAllById(accountIds).stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "channelAccounts", allEntries = true)
    public ChannelAccountRel bind(Long channelId, Long accountId) {
        // 先查找包含已删除的数据
        ChannelAccountRel rel = relRepository.findByChannelIdAndAccountId(channelId, accountId).orElse(null);
        if (rel == null) {
            rel = new ChannelAccountRel();
            rel.setChannelId(channelId);
            rel.setAccountId(accountId);
            // 这里修改为直接审批通过 (EFFECTIVE)
            rel.setStatus(ApprovalState.EFFECTIVE.name());
            rel = relRepository.save(rel);
        } else if (rel.getIsDeleted()) {
            rel.setIsDeleted(false);
            rel.setStatus(ApprovalState.EFFECTIVE.name());
            rel = relRepository.save(rel);
        }
        return rel;
    }

    @Transactional
    @CacheEvict(value = "channelAccounts", allEntries = true)
    public void unbind(Long channelId, Long accountId) {
        ChannelAccountRel rel = relRepository.findByChannelIdAndAccountIdAndIsDeletedFalse(channelId, accountId).orElseThrow();
        rel.setIsDeleted(true);
        relRepository.save(rel);
        eventPublisher.publishEvent(new RelChangeEvent(rel));
    }

    @Transactional
    @CacheEvict(value = "channelAccounts", allEntries = true)
    public void approveRel(Long relId) {
        ChannelAccountRel rel = relRepository.findById(relId).orElseThrow();
        rel.setStatus(ApprovalState.EFFECTIVE.name());
        relRepository.save(rel);
        eventPublisher.publishEvent(new RelChangeEvent(rel));
    }
}

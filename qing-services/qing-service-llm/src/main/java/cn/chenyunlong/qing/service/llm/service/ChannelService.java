package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.enums.ApprovalState;
import cn.chenyunlong.qing.service.llm.event.ChannelChangeEvent;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Cacheable(value = "channels", key = "'all_effective'")
    public List<Channel> getAllEffectiveChannels() {
        return channelRepository.findByIsDeletedFalseAndIsEnabledTrueAndStatus(ApprovalState.EFFECTIVE.name());
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findByIsDeletedFalse();
    }

    @Transactional
    @CacheEvict(value = "channels", allEntries = true)
    public Channel createOrUpdateChannel(Channel channel) {
        if (channel.getId() == null) {
            channel.setStatus(ApprovalState.EFFECTIVE.name()); // 直接生效
        } else {
            // 更新时保留原来的状态和其他不变字段
            Channel existing = channelRepository.findById(channel.getId()).orElseThrow();
            existing.setCode(channel.getCode());
            existing.setName(channel.getName());
            existing.setIcon(channel.getIcon());
            existing.setIsEnabled(channel.getIsEnabled());
            return channelRepository.save(existing);
        }
        return channelRepository.save(channel);
    }

    @Transactional
    @CacheEvict(value = "channels", allEntries = true)
    public void deleteChannel(Long id) {
        Channel channel = channelRepository.findById(id).orElseThrow();
        channel.setIsDeleted(true);
        channelRepository.save(channel);
        eventPublisher.publishEvent(new ChannelChangeEvent(channel));
    }

    @Transactional
    @CacheEvict(value = "channels", allEntries = true)
    public void submitApproval(Long id) {
        Channel channel = channelRepository.findById(id).orElseThrow();
        channel.setStatus(ApprovalState.PENDING.name());
        channelRepository.save(channel);
    }

    @Transactional
    @CacheEvict(value = "channels", allEntries = true)
    public void approve(Long id) {
        Channel channel = channelRepository.findById(id).orElseThrow();
        channel.setStatus(ApprovalState.EFFECTIVE.name());
        channelRepository.save(channel);
        eventPublisher.publishEvent(new ChannelChangeEvent(channel));
    }

    @Transactional
    @CacheEvict(value = "channels", allEntries = true)
    public void reject(Long id) {
        Channel channel = channelRepository.findById(id).orElseThrow();
        channel.setStatus(ApprovalState.REJECTED.name());
        channelRepository.save(channel);
    }
}

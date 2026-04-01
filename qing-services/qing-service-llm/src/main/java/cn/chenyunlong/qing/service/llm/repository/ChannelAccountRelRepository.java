package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.ChannelAccountRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelAccountRelRepository extends JpaRepository<ChannelAccountRel, Long> {
    List<ChannelAccountRel> findByChannelIdAndIsDeletedFalse(Long channelId);
    List<ChannelAccountRel> findByAccountIdAndIsDeletedFalse(Long accountId);
    Optional<ChannelAccountRel> findByChannelIdAndAccountIdAndIsDeletedFalse(Long channelId, Long accountId);
    Optional<ChannelAccountRel> findByChannelIdAndAccountId(Long channelId, Long accountId);
    List<ChannelAccountRel> findByChannelIdInAndIsDeletedFalseAndStatus(List<Long> channelIds, String status);
}

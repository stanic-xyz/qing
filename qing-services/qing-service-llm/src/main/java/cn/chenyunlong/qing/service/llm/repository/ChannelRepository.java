package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>, JpaSpecificationExecutor<Channel> {
    Optional<Channel> findByCode(String code);

    List<Channel> findByIsDeletedFalse();

    List<Channel> findByIsDeletedFalseAndIsEnabledTrueAndStatus(String status);

    List<Channel> findAllByIsDeletedFalseAndCodeIn(List<String> channelCodes);
}

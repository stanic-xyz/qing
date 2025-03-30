package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AttachmentEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentJpaRepository extends BaseJpaQueryRepository<AttachmentEntity, Long> {

}

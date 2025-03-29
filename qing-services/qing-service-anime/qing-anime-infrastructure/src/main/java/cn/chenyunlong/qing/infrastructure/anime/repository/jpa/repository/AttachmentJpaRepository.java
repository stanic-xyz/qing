package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.AttachmentEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentJpaRepository extends BaseJpaQueryRepository<AttachmentEntity, Long> {

}

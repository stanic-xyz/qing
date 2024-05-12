package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentJpaRepository extends BaseJpaRepository<Attachment, Long> {

}

package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.AttachmentJpaRepository;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import org.springframework.stereotype.Service;

@Service
public class AttachmentRepositoryImpl extends JpaServiceImpl<AttachmentJpaRepository, Attachment, Long> implements AttachmentRepository {

}

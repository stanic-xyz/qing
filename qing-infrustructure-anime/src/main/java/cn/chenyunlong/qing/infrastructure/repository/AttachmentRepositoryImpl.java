package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import cn.chenyunlong.qing.domain.anime.attachement.repository.AttachmentRepository;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AttachmentJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AttachmentRepositoryImpl extends JpaServiceImpl<AttachmentJpaRepository, Attachment, Long> implements AttachmentRepository {

}

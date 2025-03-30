package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.attachement.Attachment;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttachmentRepositoryImpl implements AttachmentRepository {

    @Override
    public Attachment save(Attachment entity) {
        return null;
    }

    @Override
    public Optional<Attachment> findById(AggregateId id) {
        return Optional.empty();
    }
}

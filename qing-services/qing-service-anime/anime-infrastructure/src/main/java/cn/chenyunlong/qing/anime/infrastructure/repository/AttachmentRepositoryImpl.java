package cn.chenyunlong.qing.anime.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Service;

import cn.chenyunlong.qing.anime.domain.attachement.Attachment;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentId;
import cn.chenyunlong.qing.anime.domain.attachement.AttachmentRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AttachmentEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AttachmentJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentRepositoryImpl implements AttachmentRepository {

    private final AttachmentJpaRepository attachmentJpaRepository;

    @Override
    public Attachment save(Attachment entity) {
        AttachmentEntity attachmentEntity = toEntity(entity);
        AttachmentEntity saved = attachmentJpaRepository.save(attachmentEntity);
        return toDomain(saved);
    }

    @Override
    public Optional<Attachment> findById(AttachmentId id) {
        return attachmentJpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    private AttachmentEntity toEntity(Attachment domain) {
        AttachmentEntity entity = new AttachmentEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        entity.setMimeType(domain.getMimeType());
        entity.setFileName(domain.getFileName());
        entity.setFileSize(domain.getFileSize());
        entity.setPath(domain.getPath());
        entity.setStoragePath(domain.getStoragePath());
        entity.setStorageType(domain.getStorageType());
        entity.setContentHash(domain.getContentHash());
        entity.setUploadTime(domain.getUploadTime());
        return entity;
    }

    private Attachment toDomain(AttachmentEntity entity) {
        Attachment domain = new Attachment();
        domain.setId(AttachmentId.of(entity.getId()));
        domain.setMimeType(entity.getMimeType());
        domain.setFileName(entity.getFileName());
        domain.setFileSize(entity.getFileSize());
        domain.setPath(entity.getPath());
        domain.setStoragePath(entity.getStoragePath());
        domain.setStorageType(entity.getStorageType());
        domain.setContentHash(entity.getContentHash());
        domain.setUploadTime(entity.getUploadTime());
        return domain;
    }
}

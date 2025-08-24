package cn.chenyunlong.qing.anime.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.models.TagId;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.TagEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public boolean existsByName(String name) {
        return tagJpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndNotId(String name, Long id) {
        return tagJpaRepository.existsByNameAndNotId(name, id);
    }

    @Override
    public List<Tag> findByIds(List<Long> tagIds) {
        return List.of();
    }

    @Override
    public Optional<Tag> findById(TagId id) {
        return tagJpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Tag save(Tag entity) {
        TagEntity tagEntity = toEntity(entity);
        TagEntity saved = tagJpaRepository.save(tagEntity);
        return toDomain(saved);
    }

    private Tag toDomain(TagEntity entity) {
        Tag tag = new Tag();
        tag.setId(TagId.of(entity.getId()));
        tag.setName(entity.getName());
        tag.setInstruction(entity.getInstruction());
        tag.setCreatedAt(entity.getCreatedAt());
        tag.setUpdatedAt(entity.getUpdatedAt());
        tag.setValidStatus(entity.getValidStatus());
        return tag;
    }

    private TagEntity toEntity(Tag tag) {
        TagEntity entity = new TagEntity();
        if (tag.getId() != null) {
            entity.setId(tag.getId().getValue());
        }
        entity.setName(tag.getName());
        entity.setInstruction(tag.getInstruction());
        entity.setCreatedAt(tag.getCreatedAt());
        entity.setUpdatedAt(tag.getUpdatedAt());
        entity.setValidStatus(tag.getValidStatus());
        return entity;
    }
}

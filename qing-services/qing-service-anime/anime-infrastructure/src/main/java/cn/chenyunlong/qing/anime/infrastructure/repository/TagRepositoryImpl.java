package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.TagJpaRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Tag save(Tag entity) {
        return null;
    }

    @Override
    public Optional<Tag> findById(AggregateId id) {
        return Optional.empty();
    }
}

package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.anime.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.TagJpaRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    public TagRepositoryImpl(
        @Autowired
        TagJpaRepository tagJpaRepository) {
        this.tagJpaRepository = tagJpaRepository;
    }

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

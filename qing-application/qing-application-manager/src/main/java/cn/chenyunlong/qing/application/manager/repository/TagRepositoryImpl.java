package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.TagJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagRepositoryImpl extends JpaServiceImpl<TagJpaRepository, Tag, Long> implements TagRepository {

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
}

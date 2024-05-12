package cn.chenyunlong.qing.application.manager.repository;


import cn.chenyunlong.qing.application.manager.repository.jpa.AnimeJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeCategoryRepositoryImpl implements AnimeCategoryRepository {

    private final AnimeJpaRepository animeJpaRepository;

    @Override
    public AnimeCategory save(AnimeCategory entity) {
        return null;
    }

    @Override
    public Optional<AnimeCategory> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Page<AnimeCategory> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public List<AnimeCategory> findByIds(List<Long> ids) {
        return List.of();
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {

    }

    @Override
    public void saveAll(List<AnimeCategory> domainList) {

    }
}

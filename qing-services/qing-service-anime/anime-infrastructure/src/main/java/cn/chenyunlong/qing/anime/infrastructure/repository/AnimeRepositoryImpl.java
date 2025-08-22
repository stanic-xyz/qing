package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeJpaRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeRepositoryImpl implements AnimeRepository {

    private final AnimeJpaRepository animeJpaRepository;


    @Override
    public Optional<Anime> findById(AnimeId aggregateId) {
        return animeJpaRepository.findById(aggregateId.getId())
                .map(AnimeMapper.INSTANCE::entityToDomain);
    }

    /**
     * 根据名称查询
     *
     * @param name 动漫名称
     */
    @Override
    public boolean existsByName(String name) {
        return animeJpaRepository.existsByName(name);
    }

    /**
     * 保存动漫
     *
     * @param anime 动漫
     */
    @Override
    public Anime save(Anime anime) {
        AnimeEntity animeEntity = AnimeMapper.INSTANCE.domainToEntity(anime);
        AnimeEntity saved = animeJpaRepository.save(animeEntity);
        if (anime.getId() == null) {
            anime.setId(new AggregateId(saved.getId()));
        }
        return anime;
    }

    @Override
    public List<Anime> findByIds(List<Long> animeIds) {
        List<AnimeEntity> animeEntityList = animeJpaRepository.findAllById(animeIds);
        return animeEntityList.stream().map(AnimeMapper.INSTANCE::entityToDomain).toList();
    }
}

package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.anime.application.service.IAnimeService;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.anime.domain.anime.factory.AnimeFactory;
import cn.chenyunlong.qing.anime.domain.anime.models.*;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.district.District;
import cn.chenyunlong.qing.anime.domain.district.repository.DistrictRepository;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeServiceImpl implements IAnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final DistrictRepository districtRepository;
    private final TagRepository tagRepository;
    private final TypeRepository typeRepository;

    /**
     * createImpl
     */
    @Override
    public Anime createAnime(AnimeCreator creator) {
        // 检查名称是否存在
        if (animeRepository.existsByName(creator.getName())) {
            throw new NotFoundException("动漫名称已存在");
        }

        List<Tag> tagList = tagRepository.findByIds(creator.getTagIds());
        Tags tags = Tags.create(tagList.stream().map(Tag::getName).collect(Collectors.toList()));

        District district = districtRepository.findById(new AggregateId(creator.getDistrictId())).orElseThrow(() -> new NotFoundException("地区不存在"));

        Type type = typeRepository.findById(creator.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));

        AnimeType animeType = new AnimeType(type.getAggregateId(), type.getName());

        AnimeDistrict animeDistrict = new AnimeDistrict(new AggregateId(district.getAggregateId().getId()), district.getName());

        Category category = categoryRepository.findById(creator.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));
        AnimeCategory animeCategory = new AnimeCategory(category.getAggregateId(), category.getName());

        Company company = Company.create(creator.getCompanyId(), "creator.getCompanyName()");

        Optional<Anime> optionalAnime = EntityOperations.doCreate(animeRepository)
            .create(() -> {
                long id = IdUtil.getSnowflakeNextId();
                return AnimeFactory.createAnime(
                    id,
                    creator.getName(),
                    creator.getInstruction(),
                    animeDistrict,
                    animeType,
                    company,
                    new PremiereDate(LocalDate.now()),
                    tags,
                    animeCategory);
            })
            .update(Anime::create)
            .successHook(animeInfo -> log.info("动漫信息添加成功，动漫Id：{}", animeInfo.getAggregateId().getId()))
            .execute();
        return optionalAnime.orElseThrow();
    }

    public void putOnShelf(Long animeId) {
        Anime anime = animeRepository.findById(new AnimeId(animeId))
            .orElseThrow(() -> new IllegalArgumentException("动漫不存在"));
        anime.putOnShelf();
        animeRepository.save(anime);
    }

    public void takeOffShelf(Long animeId) {
        Anime anime = animeRepository.findById(new AnimeId(animeId))
            .orElseThrow(() -> new IllegalArgumentException("动漫不存在"));
        anime.takeOffShelf();
        animeRepository.save(anime);
    }

    public void deleteAnime(Long animeId) {
        Anime anime = animeRepository.findById(new AnimeId(animeId))
            .orElseThrow(() -> new IllegalArgumentException("动漫不存在"));
        anime.delete();
        animeRepository.save(anime);
    }


    /**
     * update
     */
    @Override
    public void updateAnime(AnimeUpdater updater) {
        Optional<Anime> animeOptional = animeRepository.findById(new AnimeId(updater.getId()));
        List<Tag> tagList = tagRepository.findByIds(updater.getTags());
        District district = districtRepository.findById(new AggregateId(updater.getDistrictId())).orElseThrow(() -> new NotFoundException("地区不存在"));
        AnimeDistrict animeDistrict = new AnimeDistrict(district.getAggregateId(), district.getName());
        Category category = categoryRepository.findById(updater.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));

        EntityOperations.doUpdate(animeRepository)
            .load(animeOptional::get)
            .update(updater::updateAnime)
            .execute();
    }

    @Override
    public void validAnime(Long id) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(new AnimeId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidAnime(Long id) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(new AnimeId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    @Override
    public void removeById(Long id) {
        Optional<Anime> animeOptional = animeRepository.findById(new AnimeId(id));
        EntityOperations.doUpdate(animeRepository)
            .load(animeOptional::get)
            .update(Anime::delete)
            .execute();
    }

}

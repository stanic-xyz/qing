package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import cn.hutool.core.lang.Assert;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeServiceImpl implements IAnimeService {

    @Resource
    private final AnimeRepository animeRepository;

    @Resource
    private final AnimeCategoryRepository categoryRepository;

    @Resource
    private final DistrictRepository districtRepository;

    private final Validator validator;

    /**
     * createImpl
     */
    @Override
    public void createAnime(AnimeCreateContext createContext) {
        EntityOperations.doCreate(animeRepository)
            .create(() -> AnimeMapper.INSTANCE.creatorToEntity(createContext.getAnimeCreator()))
            .update(Anime::init)
            .successHook(animeInfo -> log.info("动漫信息添加成功，动漫Id：{}", animeInfo.getId()))
            .execute()
            .ifPresent(animeInfo -> {
                createContext.setAnime(animeInfo);
                saveRel(createContext, createContext.getTagList());
            });

    }

    /**
     * 保存标签信息
     */
    private void saveRel(AnimeCreateContext createContext, List<Tag> tags) {
        createContext.setTagList(tags);
    }

    /**
     * update
     */
    @Override
    public void updateAnime(AnimeUpdater updater) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(updater.getId())
            .update(anime -> {
                updater.updateAnime(anime);
                if (!Objects.equals(anime.getTypeId(), updater.getTypeId())) {
                    Long typeId = updater.getTypeId();
                    Optional<AnimeCategory> animeCategory = categoryRepository.findById(typeId);
                    Assert.isTrue(animeCategory.isPresent(), "分类信息不存在");
                    animeCategory.ifPresent(category -> updater.setTypeName(category.getName()));
                }
                if (!Objects.equals(anime.getDistrictId(), updater.getDistrictId())) {
                    Long districtId = updater.getDistrictId();
                    Optional<District> district = districtRepository.findById(districtId);
                    Assert.isTrue(district.isPresent(), "地区信息不存在");
                    district.ifPresent(districtName -> updater.setDistrictName(districtName.getName()));
                }
            })
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validAnime(Long id) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidAnime(Long id) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    @Override
    public void removeById(Long id) {
        animeRepository.deleteById(id);
    }

    @Override
    public AnimeVO findById(Long id) {
        return animeRepository.findById(id).map(AnimeMapper.INSTANCE::entityToVo
        ).orElse(null);
    }

    /**
     * 分页查询
     */
    @Override
    public Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> wrapper) {
        PageRequest pageRequest =
            PageRequest.of(wrapper.getPage(), wrapper.getPageSize(), Sort.Direction.DESC, "createdAt");
        return animeRepository.findAll(pageRequest).map(AnimeMapper.INSTANCE::entityToVo);
    }
}

package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
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
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final Validator validator;
    private final DistrictRepository districtRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAnime(AnimeCreateContext createContext) {
        Optional<Anime> anime = EntityOperations.doCreate(animeRepository)
                                    .create(() -> AnimeMapper.INSTANCE.creatorToEntity(createContext.getAnimeCreator()))
                                    .update(Anime::init)
                                    .successHook(animeInfo -> log.info("动漫信息添加成功，动漫Id：{}", animeInfo.getId()))
                                    .execute();
        saveRel(anime, createContext.getTagList());
        return anime.isPresent() ? anime.get().getId() : 0;
    }

    private void saveRel(Optional<Anime> anime, List<Tag> tags) {


    }

    /**
     * update
     */
    @Override
    public void updateAnime(AnimeUpdater updater) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(updater.getId())
            .update(param -> {
                if (!Objects.equals(param.getTypeId(), updater.getTypeId())) {
                    Long typeId = updater.getTypeId();
                    Optional<AnimeCategory> animeCategory = categoryRepository.findById(typeId);
                    Assert.isTrue(animeCategory.isPresent(), "分类信息不存在");
                    animeCategory.ifPresent(category -> updater.setTypeName(category.getName()));
                }
                updater.updateAnime(param);
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

    /**
     * findById
     */
    @Override
    public AnimeVO findById(Long id) {
        Optional<Anime> anime = animeRepository.findById(id);
        return anime.map(AnimeMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFindError));
    }

    @Override
    public void removeById(Long id) {
        animeRepository.deleteById(id);
    }

    /**
     * findByPage
     */
    @Override
    public Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return animeRepository.findAll(pageRequest).map(AnimeMapper.INSTANCE::entityToVo);
    }

    @Override
    public List<AnimeVO> queryLatestUpdate() {
        List<Anime> animeList = animeRepository.findAll(Sort.by("createdAt").ascending());
        Map<Long, District> districtMap = null;

        if (!animeList.isEmpty()) {
            List<Long> districtIdList = animeList.stream().map(Anime::getDistrictId).toList();
            List<District> districtList;
            if (!districtIdList.isEmpty()) {
                districtList = districtRepository.findAllById(districtIdList);
            } else {
                districtList = CollUtil.toList();
            }
            districtMap = districtList.stream().collect(Collectors.toMap(District::getId, o -> o));
        }
        if (districtMap == null) {
            districtMap = MapUtil.newHashMap();
        }

        Map<Long, District> finalDistrictMap = districtMap;
        return animeList.stream().map(animeInfo -> {
            AnimeVO animeVO = AnimeMapper.INSTANCE.entityToVo(animeInfo);
            if (finalDistrictMap.containsKey(animeVO.getDistrictId())) {
                animeVO.setDistrictName(finalDistrictMap.get(animeVO.getDistrictId()).getName());
            }
            return animeVO;
        }).toList();

    }
}

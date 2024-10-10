package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.BaseJpaService;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeDetailVO;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.anime.mapper.TagMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeTagRelRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.repository.DistrictRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.mapper.EpisodeMapper;
import cn.chenyunlong.qing.domain.anime.episode.repository.EpisodeRepository;
import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import cn.chenyunlong.qing.domain.anime.playlist.dto.vo.PlayListVO;
import cn.chenyunlong.qing.domain.anime.playlist.mapper.PlayListMapper;
import cn.chenyunlong.qing.domain.anime.playlist.repository.PlayListRepository;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeServiceImpl extends BaseJpaService implements IAnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final DistrictRepository districtRepository;
    private final TagRepository tagRepository;
    private final AnimeTagRelRepository animeTagRelRepository;
    private final PlayListRepository playListRepository;
    private final EpisodeRepository episodeRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAnime(AnimeCreator creator) {
        List<Tag> tagList = tagRepository.findByIds(creator.getTagIds());
        District district = districtRepository.findById(creator.getDistrictId()).orElseThrow(() -> new NotFoundException("地区不存在"));
        AnimeCategory animeCategory = categoryRepository.findById(creator.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));
        // 检查名称是否存在
        if (animeRepository.existsByName(creator.getName()) > 0) {
            throw new NotFoundException("动漫名称已存在");
        }
        Optional<Anime> optionalAnime = doCreate(animeRepository)
            .create(() -> creator.create(tagList, district, animeCategory))
            .update(Anime::create)
            .successHook(animeInfo -> log.info("动漫信息添加成功，动漫Id：{}", animeInfo.getId()))
            .execute();
        Anime anime = optionalAnime.orElseThrow();
        saveRel(anime, tagList);
        return optionalAnime.map(BaseJpaAggregate::getId).orElse(null);
    }

    /**
     * 保存关联信息
     *
     * @param anime   动漫信息
     * @param tagList tag列表
     */
    private void saveRel(Anime anime, List<Tag> tagList) {
        List<AnimeTagRel> animeTagRelList = tagList.stream().map(tag -> new AnimeTagRel(null, anime.getId(), tag.getId())).collect(Collectors.toList());
        animeTagRelRepository.saveAll(animeTagRelList);
    }

    /**
     * update
     */
    @Override
    public void updateAnime(AnimeUpdater updater) {
        Optional<Anime> animeOptional = animeRepository.findById(updater.getId());
        List<Tag> tagList = tagRepository.findByIds(updater.getTags());
        District district = districtRepository.findById(updater.getDistrictId()).orElseThrow(() -> new NotFoundException("地区不存在"));
        AnimeCategory animeCategory = categoryRepository.findById(updater.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));
        doUpdate(animeRepository)
            .load(animeOptional::get)
            .update(anime -> updater.updateAnime(anime, tagList, district, animeCategory))
            .execute();
    }

    @Override
    public void validAnime(Long id) {
        doUpdate(animeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    @Override
    public void invalidAnime(Long id) {
        doUpdate(animeRepository)
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
     * 根据Id查询
     *
     * @param id 根据Id查询详情
     */
    @Override
    public AnimeDetailVO findDetailById(Long id) {
        return animeRepository.findById(id).map(anime -> {
                AnimeDetailVO animeDetailVO = AnimeMapper.INSTANCE.entityToDetailVo(anime);
                List<AnimeTagRel> animeTagRelList = animeTagRelRepository.listTagByAnimeId(anime.getId());

                List<Tag> tagList = tagRepository.findByIds(animeTagRelList.stream().map(AnimeTagRel::getTagId).collect(Collectors.toList()));
                animeDetailVO.setTagVOList(tagList.stream().map(TagMapper.INSTANCE::entityToVo).collect(Collectors.toList()));

            List<PlayList> playListList = playListRepository.listByAnime(anime.getId());

            List<Episode> episodeList = episodeRepository.listByAnimeId(anime.getId());
            Map<Long, List<Episode>> longListMap = episodeList.stream().collect(Collectors.groupingBy(Episode::getPlayListId));

            animeDetailVO.setPlayLists(playListList.stream().map(playList -> {
                PlayListVO playListVO = PlayListMapper.INSTANCE.entityToVo(playList);
                List<Episode> episodes = longListMap.getOrDefault(playList.getId(), CollUtil.toList());
                playListVO.setEpisodeList(episodes.stream().map(EpisodeMapper.INSTANCE::entityToVo).collect(Collectors.toList()));
                return playListVO;
            }).collect(Collectors.toList()));
                return animeDetailVO;
            }
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

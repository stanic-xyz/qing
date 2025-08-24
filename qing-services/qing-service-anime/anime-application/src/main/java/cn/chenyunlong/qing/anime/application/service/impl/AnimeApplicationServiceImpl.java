package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.qing.anime.application.command.AnimeStatusCommand;
import cn.chenyunlong.qing.anime.application.command.CreateAnimeCommand;
import cn.chenyunlong.qing.anime.application.command.UpdateAnimeCommand;
import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.application.query.AnimeQuery;
import cn.chenyunlong.qing.anime.application.service.AnimeApplicationService;
import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.models.*;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.anime.models.TagId;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动漫应用服务实现
 *
 * <p>
 * 实现动漫相关的应用服务操作，协调领域对象完成业务用例
 * </p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeApplicationServiceImpl implements AnimeApplicationService {

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public AnimeDTO createAnime(CreateAnimeCommand command) {
        log.info("开始创建动漫，名称: {}", command.getName());

        // 验证命令
        command.validate();

        // 检查名称是否重复
        if (animeRepository.existsByName(command.getCleanName())) {
            throw new IllegalStateException("动漫名称已存在: " + command.getCleanName());
        }

        // 验证分类是否存在
        if (!categoryRepository.existsById(command.getCategoryId())) {
            throw new IllegalArgumentException("分类不存在: " + command.getCategoryId());
        }

        // 验证标签是否存在
        Tags tags = validateAndCreateTags(command.getTagIds());

        // 创建动漫聚合根
        Anime anime = Anime.create(AnimeId.of(IdUtil.getSnowflakeNextId()), command.getName(), new AnimeCategory(CategoryId.of(IdUtil.getSnowflakeNextId()), ""), command.getInstruction());

        // 保存到仓储
        Anime savedAnime = animeRepository.save(anime);

        log.info("动漫创建成功，ID: {}, 名称: {}", savedAnime.getId().getValue(), savedAnime.getName());

        return convertToDTO(savedAnime);
    }

    @Override
    @Transactional
    public AnimeDTO updateAnime(UpdateAnimeCommand command) {
        log.info("开始更新动漫，ID: {}", command.getAnimeId());

        // 验证命令
        command.validate();

        if (!command.hasUpdates()) {
            throw new IllegalArgumentException("没有需要更新的字段");
        }

        // 加载动漫聚合根
        Anime anime = loadAnimeById(command.getAnimeId());

        // 检查名称是否重复（如果名称有变更）
        if (StringUtils.hasText(command.getCleanName()) &&
                !command.getCleanName().equals(anime.getName()) &&
                animeRepository.existsByName(command.getCleanName())) {
            throw new IllegalStateException("动漫名称已存在: " + command.getCleanName());
        }

        // 更新基本信息
        if (StringUtils.hasText(command.getCleanName()) || StringUtils.hasText(command.getCleanInstruction())) {
            anime.updateInfo(command.getCleanName(), command.getCleanInstruction(), command.getCoverUrl(), command.getOriginalName(), command.getOtherName(), command.getAuthor(), command.getOfficialWebsite());
        }

        // 更新详细信息
        anime.updateDetails(District.CHINA, new AnimeType(TypeId.of(1L), "測ui"), Company.create(1L, "攻擊"), new PremiereDate(LocalDate.now()),
                PlotTypes.empty(), "", 1
        );

        // 更新标签
        if (command.getTagIds() != null) {
            Tags newTags = validateAndCreateTags(command.getTagIds());
            anime.setTags(newTags.asList());
        }

        // 更新播放状态
        if (command.getPlayStatus() != null) {
            PlayStatus playStatus = PlayStatus.of(command.getPlayStatus()).orElseThrow();
            anime.changePlayStatus(playStatus);
        }

        // 保存到仓储
        Anime savedAnime = animeRepository.save(anime);

        log.info("动漫更新成功，ID: {}, 名称: {}", savedAnime.getId().getValue(), savedAnime.getName());

        return convertToDTO(savedAnime);
    }

    @Override
    @Transactional
    public AnimeDTO executeStatusOperation(AnimeStatusCommand command) {
        log.info("开始执行动漫状态操作，ID: {}, 操作: {}", command.getAnimeId(), command.getOperation());

        // 验证命令
        command.validate();

        // 加载动漫聚合根
        Anime anime = loadAnimeById(command.getAnimeId());

        // 执行状态操作
        switch (command.getOperation()) {
            case PUT_ON_SHELF:
                anime.putOnShelf();
                break;
            case TAKE_OFF_SHELF:
                anime.takeOffShelf();
                break;
            case DELETE:
                anime.delete();
                break;
            case RESTORE:
                anime.restore();
                break;
            default:
                throw new IllegalArgumentException("不支持的操作类型: " + command.getOperation());
        }

        // 保存到仓储
        Anime savedAnime = animeRepository.save(anime);

        log.info("动漫状态操作成功，ID: {}, 操作: {}", savedAnime.getId().getValue(), command.getOperation());

        return convertToDTO(savedAnime);
    }

    @Override
    public Optional<AnimeDTO> findAnimeById(Long animeId) {
        if (animeId == null || animeId <= 0) {
            throw new IllegalArgumentException("动漫ID不能为空且必须大于0");
        }

        return animeRepository.findById(AnimeId.of(animeId))
                .map(this::convertToDTO);
    }

    @Override
    public List<AnimeDTO> findAnimesByIds(List<Long> animeIds) {
        if (animeIds == null || animeIds.isEmpty()) {
            return Collections.emptyList();
        }

        if (animeIds.size() > 100) {
            throw new IllegalArgumentException("动漫ID列表不能超过100个");
        }

        List<AnimeId> ids = animeIds.stream()
                .map(AnimeId::of)
                .collect(Collectors.toList());

        return animeRepository.findByAnimeIds(ids).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<AnimeDTO> findAnimes(AnimeQuery query) {
        query.validate();

        // 这里需要根据具体的仓储实现来查询
        // 暂时返回空结果，具体实现需要在Infrastructure层完成
        return PageResult.empty(query.getPage(), query.getSize());
    }

    @Override
    public PageResult<AnimeDTO> findOnShelfAnimes(Integer page, Integer size) {
        AnimeQuery query = AnimeQuery.onShelf();
        if (page != null) {
            query.setPage(page);
        }
        if (size != null) {
            query.setSize(size);
        }
        return findAnimes(query);
    }

    @Override
    public PageResult<AnimeDTO> findAnimesByCategory(Long categoryId, Integer page, Integer size) {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("分类ID不能为空且必须大于0");
        }

        AnimeQuery query = AnimeQuery.byCategory(categoryId);
        if (page != null) {
            query.setPage(page);
        }
        if (size != null) {
            query.setSize(size);
        }
        return findAnimes(query);
    }

    @Override
    public PageResult<AnimeDTO> findAnimesByTag(Long tagId, Integer page, Integer size) {
        if (tagId == null || tagId <= 0) {
            throw new IllegalArgumentException("标签ID不能为空且必须大于0");
        }

        AnimeQuery query = AnimeQuery.byTag(tagId);
        if (page != null) {
            query.setPage(page);
        }
        if (size != null) {
            query.setSize(size);
        }
        return findAnimes(query);
    }

    @Override
    public PageResult<AnimeDTO> findAnimesByName(String name, Integer page, Integer size) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("动漫名称不能为空");
        }

        AnimeQuery query = AnimeQuery.builder()
                .name(name.trim())
                .deleted(false)
                .page(page != null ? page : 1)
                .size(size != null ? size : 20)
                .build();

        return findAnimes(query);
    }

    @Override
    public Long countAllAnimes() {
        return animeRepository.countAll();
    }

    @Override
    public Long countOnShelfAnimes() {
        return animeRepository.countOnShelf();
    }

    @Override
    public Long countAnimesByCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("分类ID不能为空且必须大于0");
        }

        return categoryRepository.findById(CategoryId.of(categoryId))
                .map(category -> {
                    AnimeCategory animeCategory = new AnimeCategory(category.getId(), category.getName());
                    return animeRepository.countByCategory(animeCategory);
                })
                .orElse(0L);
    }

    @Override
    public Boolean existsByName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("动漫名称不能为空");
        }

        return animeRepository.existsByName(name.trim());
    }

    @Override
    public Boolean existsByNameExcludeId(String name, Long excludeId) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("动漫名称不能为空");
        }

        if (excludeId == null || excludeId <= 0) {
            throw new IllegalArgumentException("排除ID不能为空且必须大于0");
        }

        // 这里需要在仓储中实现相应的方法
        // 暂时使用简单的实现
        return animeRepository.existsByName(name.trim());
    }

    /**
     * 根据ID加载动漫聚合根
     *
     * @param animeId 动漫ID
     * @return 动漫聚合根
     * @throws IllegalStateException 当动漫不存在时
     */
    private Anime loadAnimeById(Long animeId) {
        return animeRepository.findById(AnimeId.of(animeId))
                .orElseThrow(() -> new IllegalStateException("动漫不存在: " + animeId));
    }

    /**
     * 验证并创建标签值对象
     *
     * @param tagIds 标签ID列表
     * @return 标签值对象
     */
    private Tags validateAndCreateTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Tags.createEmptyTags();
        }

        // 验证标签是否存在
        List<String> tagNames = tagIds.stream()
                .map(tagId -> {
                    Optional<Tag> tag = tagRepository.findById(TagId.of(tagId));
                    if (tag.isEmpty()) {
                        throw new IllegalArgumentException("标签不存在: " + tagId);
                    }
                    return tag.get().getName();
                })
                .collect(Collectors.toList());

        return Tags.create(tagNames);
    }

    /**
     * 将动漫聚合根转换为DTO
     *
     * @param anime 动漫聚合根
     * @return 动漫DTO
     */
    private AnimeDTO convertToDTO(Anime anime) {
        return AnimeDTO.builder()
                .id(anime.getId().getValue())
                .name(anime.getName())
                .instruction(anime.getInstruction())
                .categoryId(anime.getAnimeCategory() != null ? anime.getAnimeCategory().id().getValue() : null)
                .tags(anime.getTags() != null ? anime.getTags().tags() : null)
                .playStatus(anime.getPlayStatus() != null ? anime.getPlayStatus().name() : null)
                .coverUrl(anime.getCover())
                .originalName(anime.getOriginalName())
                .otherName(anime.getOtherName())
                .author(anime.getAuthor())
                .officialWebsite(anime.getOfficialWebsite())
                .district(anime.getDistrict() != null ? anime.getDistrict().getName() : null)
                .companyId(anime.getCompany() != null ? anime.getCompany().companyId() : null)
                .typeId(anime.getType() != null ? anime.getType().typeId().getValue() : null)
                .onShelf(anime.isOnShelf())
                .deleted(anime.isDeleted())
                .lastUpdateTime(anime.getLastUpdateTime())
                .lastUpdatedBy(anime.getLastUpdatedBy())
                .version(anime.getVersion() != null ? anime.getVersion().longValue() : 0L)
                .build();
    }
}

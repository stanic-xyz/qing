package cn.chenyunlong.qing.anime.application.command;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.anime.application.mapper.AnimeCategoryApplicationMapper;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.models.*;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 动漫命令处理器
 * 负责处理所有动漫相关的命令操作
 *
 * @author chenyunlong
 * @since 2024-12-30
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AnimeCommandHandler {

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AnimeCategoryApplicationMapper mapper;

    /**
     * 处理创建动漫命令
     *
     * @param command 创建动漫命令
     * @return 动漫ID
     */
    public AnimeId handleCreateAnime(CreateAnimeCommand command) {
        log.info("处理创建动漫命令: {}", command.getName());

        // 验证命令
        command.validate();

        // 检查名称是否已存在
        if (animeRepository.existsByName(command.getCleanName())) {
            throw new BusinessException("动漫名称已存在: " + command.getName());
        }

        // 验证分类
        AnimeCategory category = loadCategoryById(command.getCategoryId());

        // 验证并创建标签
        validateAndCreateTags(command.getTagIds());

        // 创建动漫聚合根
        AnimeId animeId = animeRepository.nextId();
        Anime anime = Anime.create(
                animeId,
                command.getCleanName(),
                category,
                command.getCleanInstruction());

        // 设置标签
        if (command.getTagIds() != null && !command.getTagIds().isEmpty()) {
            List<String> tagNames = loadTagNamesByIds(command.getTagIds());
            anime.setTags(tagNames);
        }

        // 初始化动漫
        anime.initialize();

        // 保存动漫
        animeRepository.save(anime);

        log.info("动漫创建成功，ID: {}, 名称: {}", animeId, command.getName());
        return animeId;
    }

    /**
     * 处理更新动漫命令
     *
     * @param command 更新动漫命令
     */
    public void handleUpdateAnime(UpdateAnimeCommand command) {
        log.info("处理更新动漫命令，ID: {}", command.getAnimeId());

        // 验证命令
        command.validate();

        // 检查是否有更新内容
        if (command.hasUpdates()) {
            log.warn("没有需要更新的内容，ID: {}", command.getAnimeId());
            return;
        }

        // 加载动漫
        Anime anime = loadAnimeById(command.getAnimeId());

        // 检查名称唯一性（如果名称有变更）
        if (StringUtils.hasText(command.getName()) &&
                !command.getCleanName().equals(anime.getName())) {
            if (animeRepository.existsByNameExcludeId(command.getCleanName(), AnimeId.of(command.getAnimeId()))) {
                throw new BusinessException("动漫名称已存在: " + command.getName());
            }
        }

        // 更新基本信息
        if (hasBasicInfoUpdates(command)) {
            anime.updateInfo(
                    command.getCleanName(),
                    command.getCleanInstruction(),
                    command.getCoverUrl(),
                    command.getOriginalName(),
                    command.getOtherName(),
                    command.getAuthor(),
                    command.getOfficialWebsite());
        }

        // 更新详细信息
        if (hasDetailUpdates(command)) {
            anime.updateDetails(
                    District.CHINA,
                    // TODO 补充完整
                    new AnimeType(TypeId.of(1L), "测试名称"),
                    new Company(1L, "测试公司"),
                    new PremiereDate(LocalDate.now()),
                    new PlotTypes(CollUtil.toList("玄幻")),
                    "", 1);
        }

        // 更新标签
        if (command.getTagIds() != null) {
            validateAndCreateTags(command.getTagIds());
            List<String> tagNames = loadTagNamesByIds(command.getTagIds());
            anime.setTags(tagNames);
        }

        // 更新播放状态
        if (command.getPlayStatus() != null) {
            PlayStatus playStatus = PlayStatus.of(command.getPlayStatus()).orElseThrow();
            anime.changePlayStatus(playStatus);
        }

        // 保存动漫
        animeRepository.save(anime);
        log.info("动漫更新成功，ID: {}", command.getAnimeId());
    }

    /**
     * 处理动漫状态命令
     *
     * @param command 状态命令
     */
    public void handleAnimeStatus(AnimeStatusCommand command) {
        log.info("处理动漫状态命令，ID: {}, 操作: {}", command.getAnimeId(), command.getOperation());

        // 验证命令
        command.validate();

        // 加载动漫
        Anime anime = loadAnimeById(command.getAnimeId());

        // 根据操作类型执行相应操作
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
                throw new BusinessException("不支持的操作类型: " + command.getOperation());
        }

        // 保存动漫
        animeRepository.save(anime);

        log.info("动漫状态操作成功，ID: {}, 操作: {}", command.getAnimeId(), command.getOperation());
    }

    /**
     * 根据ID加载动漫
     *
     * @param animeId 动漫ID
     * @return 动漫聚合根
     * @throws BusinessException 如果动漫不存在
     */
    private Anime loadAnimeById(Long animeId) {
        return animeRepository.findById(AnimeId.of(animeId))
                .orElseThrow(() -> new BusinessException("动漫不存在，ID: " + animeId));
    }

    /**
     * 根据ID加载分类
     *
     * @param categoryId 分类ID
     * @return 动漫分类
     * @throws BusinessException 如果分类不存在
     */
    private AnimeCategory loadCategoryById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(CategoryId.of(categoryId));

        return category.map(mapper::toAnimeCategory)
                .orElseThrow(() -> new BusinessException("动漫分类不存在，ID: " + categoryId));

    }

    /**
     * 验证并创建标签
     *
     * @param tagIds 标签ID列表
     */
    private void validateAndCreateTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }

        // 检查标签是否存在
        List<Tag> existingTags = tagRepository.findByIds(tagIds);

        List<Long> existingTagIds = existingTags.stream()
                .map(Tag::getId)
                .map(TagId::id)
                .toList();

        // 找出不存在的标签ID
        List<Long> missingTagIds = tagIds.stream()
                .filter(id -> !existingTagIds.contains(id))
                .toList();

        if (!missingTagIds.isEmpty()) {
            throw new BusinessException("标签不存在，ID: " + missingTagIds);
        }
    }

    /**
     * 根据标签ID列表加载标签名称
     *
     * @param tagIds 标签ID列表
     * @return 标签名称列表
     */
    private List<String> loadTagNamesByIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return List.of();
        }

        return tagRepository.findByIds(tagIds)
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    /**
     * 检查是否有基本信息更新
     *
     * @param command 更新命令
     * @return 是否有基本信息更新
     */
    private boolean hasBasicInfoUpdates(UpdateAnimeCommand command) {
        return StringUtils.hasText(command.getName()) ||
                StringUtils.hasText(command.getInstruction()) ||
                StringUtils.hasText(command.getCoverUrl()) ||
                StringUtils.hasText(command.getOriginalName()) ||
                StringUtils.hasText(command.getOtherName()) ||
                StringUtils.hasText(command.getAuthor()) ||
                StringUtils.hasText(command.getOfficialWebsite());
    }

    /**
     * 检查是否有详细信息更新
     *
     * @param command 更新命令
     * @return 是否有详细信息更新
     */
    private boolean hasDetailUpdates(UpdateAnimeCommand command) {
        return command.getAuthor() != null ||
                command.getCleanName() != null ||
                command.getInstruction() != null;
    }
}

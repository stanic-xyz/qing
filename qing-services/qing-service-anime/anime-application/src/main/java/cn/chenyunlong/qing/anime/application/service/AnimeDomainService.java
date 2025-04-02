package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeRemoveCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeShelveOffCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeShelvingCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.CreatorAnimeCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdateCommand;
import cn.chenyunlong.qing.anime.domain.anime.exception.AnimeNotApprovedException;
import cn.chenyunlong.qing.anime.domain.anime.exception.CopyrightViolationException;
import cn.chenyunlong.qing.anime.domain.anime.factory.AnimeFactory;
import cn.chenyunlong.qing.anime.domain.anime.models.*;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.anime.domain.services.FileStorageService;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.repository.TypeRepository;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.chenyunlong.qing.domain.base.EntityOperations.doCreate;
import static cn.chenyunlong.qing.domain.base.EntityOperations.doUpdate;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeDomainService {

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TypeRepository typeRepository;
    private final FileStorageService fileStorageService;

    public Anime createAnime(CreatorAnimeCommand creator) {
        return doCreate(animeRepository).create(() ->
            {
                // 检查名称是否存在
                if (animeRepository.existsByName(creator.getName())) {
                    throw new NotFoundException("动漫名称已存在");
                }

                List<Tag> tagList = tagRepository.findByIds(creator.getTagIds());
                Tags tags = Tags.create(tagList.stream().map(Tag::getName).collect(Collectors.toList()));

                Type type = typeRepository.findById(creator.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));

                AnimeType animeType = new AnimeType(type.getId(), type.getName());

                Category category = categoryRepository.findById(creator.getTypeId()).orElseThrow(() -> new NotFoundException("类型不存在"));
                AnimeCategory animeCategory = new AnimeCategory(category.getId(), category.getName());

                Company company = Company.create(creator.getCompanyId(), "creator.getCompanyName()");

                long id = IdUtil.getSnowflakeNextId();
                return AnimeFactory.createAnime(
                    id,
                    creator.getName(),
                    creator.getInstruction(),
                    creator.getDistrict(),
                    animeType,
                    company,
                    new PremiereDate(LocalDate.now()),
                    tags,
                    animeCategory);
            })
            .update(Anime::create)
            .successHook(animeInfo -> log.info("动漫信息添加成功，动漫Id：{}", animeInfo.getId().getId()))
            .execute().orElseThrow();
    }

    /**
     * 执行动漫上架
     *
     * @param command 上架命令（包含动漫ID、目标平台、操作人等）
     * @throws AnimeNotApprovedException   动漫未通过审核
     * @throws CopyrightViolationException 版权校验失败
     */

    public void shelveAnime(AnimeShelvingCommand command) {
        doUpdate(animeRepository)
            .loadById(command.animeId())
            .update(Anime::putOnShelf)
            .execute();
    }

    public void takeOffShelf(AnimeShelveOffCommand shelveOffCommand) {
        doUpdate(animeRepository)
            .loadById(shelveOffCommand.animeId())
            .update(Anime::takeOffShelf)
            .execute();
    }

    public void deleteAnime(AnimeRemoveCommand command) {
        doUpdate(animeRepository)
            .loadById(command.animeId())
            .update(Anime::delete)
            .execute();
    }

    public void updateAnime(AnimeUpdateCommand updateCommand) {
        Optional<Anime> animeOptional = animeRepository.findById(updateCommand.getId());
        doUpdate(animeRepository)
            .load(animeOptional::get)
            .update(anime -> {
                if (!StrUtil.equals(anime.getName(), updateCommand.getName())) {
                    // 检查名称是否存在
                    if (animeRepository.existsByName(updateCommand.getName())) {
                        throw new NotFoundException("动漫名称已存在");
                    }
                }
                anime.updateInfo(updateCommand.getName(),
                    updateCommand.getInstruction(),
                    updateCommand.getCover(),
                    updateCommand.getOriginalName(),
                    updateCommand.getOtherName(),
                    updateCommand.getAuthor(),
                    updateCommand.getOfficialWebsite());
            })
            .execute();
    }

    public void validAnime(Long id) {
        doUpdate(animeRepository)
            .loadById(new AnimeId(id))
            .update(BaseAggregate::valid)
            .execute();
    }


    public void invalidAnime(Long id) {
        doUpdate(animeRepository)
            .loadById(new AnimeId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }
}

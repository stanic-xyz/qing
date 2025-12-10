package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.qing.anime.application.service.ITagService;
import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.TagCreator;
import cn.chenyunlong.qing.anime.domain.anime.models.TagId;
import cn.chenyunlong.qing.anime.domain.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.hutool.core.lang.Assert;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

    @Resource
    private final TagRepository tagRepository;

    /**
     * createImpl
     */
    @Override
    public Long createTag(TagCreator creator) {
        boolean existsByName = tagRepository.existsByName(creator.getName());
        Assert.isFalse(existsByName, "标签名称已存在");
        Optional<Tag> tag = EntityOperations.doCreate(tagRepository)
                .create(Tag::new)
                .update(Tag::init)
                .execute();
        return tag.isPresent() ? tag.get().getId().id() : 0;
    }

    @Override
    public void validTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
                .loadById(TagId.of(id))
                .update(Tag::valid)
                .execute();
    }

    @Override
    public void invalidTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
                .loadById(TagId.of(id))
                .update(Tag::invalid)
                .execute();
    }
}

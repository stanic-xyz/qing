package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.anime.mapper.TagMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.ITagService;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
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
            .create(() -> TagMapper.INSTANCE.dtoToEntity(creator))
            .update(Tag::init)
            .execute();
        return tag.isPresent() ? tag.get().getAggregateId().getId() : 0;
    }

    @Override
    public void validTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }
}

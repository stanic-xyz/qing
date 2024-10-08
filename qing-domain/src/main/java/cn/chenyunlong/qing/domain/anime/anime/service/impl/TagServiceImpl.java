package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.TagVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.TagMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.ITagService;
import cn.hutool.core.lang.Assert;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return tag.isPresent() ? tag.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateTag(TagUpdater updater) {
        // 判断标签是否和其他标签重名
        boolean existsByNameAndNotId = tagRepository.existsByNameAndNotId(updater.getName(), updater.getId());
        Assert.isFalse(existsByNameAndNotId, "标签名称已存在");

        EntityOperations.doUpdate(tagRepository)
            .loadById(updater.getId())
            .update(updater::updateTag)
            .execute();
    }

    @Override
    public void validTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    @Override
    public void invalidTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public TagVO findById(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        return tagOptional.map(TagMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    @Override
    public Page<TagVO> findByPage(PageRequestWrapper<TagQuery> wrapper) {
        PageRequest pageRequest =
            PageRequest.of(wrapper.getPage(), wrapper.getPageSize(), Sort.Direction.DESC, "createdAt");
        return tagRepository.findAll(pageRequest).map(TagMapper.INSTANCE::entityToVo);
    }
}

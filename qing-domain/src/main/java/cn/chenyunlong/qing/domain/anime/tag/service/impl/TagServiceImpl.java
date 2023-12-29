package cn.chenyunlong.qing.domain.anime.tag.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.tag.QTag;
import cn.chenyunlong.qing.domain.anime.tag.Tag;
import cn.chenyunlong.qing.domain.anime.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.tag.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.anime.tag.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.anime.tag.dto.vo.TagVO;
import cn.chenyunlong.qing.domain.anime.tag.mapper.TagMapper;
import cn.chenyunlong.qing.domain.anime.tag.repository.TagRepository;
import cn.chenyunlong.qing.domain.anime.tag.service.ITagService;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;
    private final TagRepository tagRepository;

    /**
     * createImpl
     */
    @Override
    public Long createTag(TagCreator creator) {
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
        EntityOperations.doUpdate(tagRepository)
            .loadById(updater.getId())
            .update(updater::updateTag)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validTag(Long id) {
        EntityOperations.doUpdate(tagRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
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
        QTag tag = QTag.tag;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);
        Tag one = query.select(tag)
            .from(tag)
            .where(tag.id.eq(1L))
            .fetchOne();
        log.info(String.valueOf(one));
        Optional<Tag> tagOptional = tagRepository.findById(id);
        return new TagVO(
            tagOptional.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<TagVO> findByPage(PageRequestWrapper<TagQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return tagRepository.findAll(pageRequest).map(TagVO::new);
    }
}

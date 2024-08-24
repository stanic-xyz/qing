package cn.chenyunlong.qing.domain.attribute.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.attribute.Attribute;
import cn.chenyunlong.qing.domain.attribute.dto.creator.AttributeCreator;
import cn.chenyunlong.qing.domain.attribute.dto.query.AttributeQuery;
import cn.chenyunlong.qing.domain.attribute.dto.updater.AttributeUpdater;
import cn.chenyunlong.qing.domain.attribute.dto.vo.AttributeVO;
import cn.chenyunlong.qing.domain.attribute.mapper.AttributeMapper;
import cn.chenyunlong.qing.domain.attribute.repository.AttributeRepository;
import cn.chenyunlong.qing.domain.attribute.service.IAttributeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AttributeServiceImpl implements IAttributeService {

    private final AttributeRepository attributeRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAttribute(AttributeCreator creator) {
        Optional<Attribute> attribute = EntityOperations.doCreate(attributeRepository)
            .create(() -> AttributeMapper.INSTANCE.dtoToEntity(creator))
            .update(Attribute::init)
            .execute();
        return attribute.isPresent() ? attribute.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateAttribute(AttributeUpdater updater) {
        EntityOperations.doUpdate(attributeRepository)
            .loadById(updater.getId())
            .update(updater::updateAttribute)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validAttribute(Long id) {
        EntityOperations.doUpdate(attributeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidAttribute(Long id) {
        EntityOperations.doUpdate(attributeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public AttributeVO findById(Long id) {
        Optional<Attribute> attribute = attributeRepository.findById(id);
        return new AttributeVO(
            attribute.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<AttributeVO> findByPage(PageRequestWrapper<AttributeQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return attributeRepository.findAll(pageRequest).map(AttributeVO::new);
    }
}

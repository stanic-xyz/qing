package cn.chenyunlong.qing.domain.productcenter.templateitem.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.templateitem.TemplateItem;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.creator.TemplateItemCreator;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.query.TemplateItemQuery;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.updater.TemplateItemUpdater;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.vo.TemplateItemVO;
import cn.chenyunlong.qing.domain.productcenter.templateitem.mapper.TemplateItemMapper;
import cn.chenyunlong.qing.domain.productcenter.templateitem.repository.TemplateItemRepository;
import cn.chenyunlong.qing.domain.productcenter.templateitem.service.ITemplateItemService;
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
public class TemplateItemServiceImpl implements ITemplateItemService {

    private final TemplateItemRepository templateItemRepository;

    /**
     * createImpl
     */
    @Override
    public Long createTemplateItem(TemplateItemCreator creator) {
        Optional<TemplateItem> templateItem = EntityOperations.doCreate(templateItemRepository)
            .create(() -> TemplateItemMapper.INSTANCE.dtoToEntity(creator))
            .update(TemplateItem::init)
            .execute();
        return templateItem.isPresent() ? templateItem.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateTemplateItem(TemplateItemUpdater updater) {
        EntityOperations.doUpdate(templateItemRepository)
            .loadById(updater.getId())
            .update(updater::updateTemplateItem)
            .execute();
    }

    @Override
    public void validTemplateItem(Long id) {
        EntityOperations.doUpdate(templateItemRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    @Override
    public void invalidTemplateItem(Long id) {
        EntityOperations.doUpdate(templateItemRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public TemplateItemVO findById(Long id) {
        Optional<TemplateItem> templateItem = templateItemRepository.findById(id);
        return new TemplateItemVO(
            templateItem.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<TemplateItemVO> findByPage(PageRequestWrapper<TemplateItemQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return templateItemRepository.findAll(pageRequest).map(TemplateItemVO::new);
    }
}

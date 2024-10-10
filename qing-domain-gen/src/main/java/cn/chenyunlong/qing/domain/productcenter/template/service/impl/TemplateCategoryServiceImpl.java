package cn.chenyunlong.qing.domain.productcenter.template.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.template.TemplateCategory;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCategoryCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateCategoryQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateCategoryUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateCategoryVO;
import cn.chenyunlong.qing.domain.productcenter.template.mapper.TemplateCategoryMapper;
import cn.chenyunlong.qing.domain.productcenter.template.repository.TemplateCategoryRepository;
import cn.chenyunlong.qing.domain.productcenter.template.service.ITemplateCategoryService;
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
public class TemplateCategoryServiceImpl implements ITemplateCategoryService {

    private final TemplateCategoryRepository templateCategoryRepository;

    /**
     * createImpl
     */
    @Override
    public Long createTemplateCategory(TemplateCategoryCreator creator) {
        Optional<TemplateCategory> templateCategory =
            EntityOperations.doCreate(templateCategoryRepository)
                .create(() -> TemplateCategoryMapper.INSTANCE.dtoToEntity(creator))
                .update(TemplateCategory::init)
                .execute();
        return templateCategory.isPresent() ? templateCategory.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateTemplateCategory(TemplateCategoryUpdater updater) {
        EntityOperations.doUpdate(templateCategoryRepository)
            .loadById(updater.getId())
            .update(updater::updateTemplateCategory)
            .execute();
    }

    @Override
    public void validTemplateCategory(Long id) {
        EntityOperations.doUpdate(templateCategoryRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    @Override
    public void invalidTemplateCategory(Long id) {
        EntityOperations.doUpdate(templateCategoryRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public TemplateCategoryVO findById(Long id) {
        Optional<TemplateCategory> templateCategory = templateCategoryRepository.findById(id);
        return new TemplateCategoryVO(
            templateCategory.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<TemplateCategoryVO> findByPage(PageRequestWrapper<TemplateCategoryQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return templateCategoryRepository.findAll(pageRequest).map(TemplateCategoryVO::new);
    }
}

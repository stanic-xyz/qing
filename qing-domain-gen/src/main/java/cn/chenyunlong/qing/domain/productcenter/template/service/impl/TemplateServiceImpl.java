package cn.chenyunlong.qing.domain.productcenter.template.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.template.Template;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateVO;
import cn.chenyunlong.qing.domain.productcenter.template.mapper.TemplateMapper;
import cn.chenyunlong.qing.domain.productcenter.template.repository.TemplateRepository;
import cn.chenyunlong.qing.domain.productcenter.template.service.ITemplateService;
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
public class TemplateServiceImpl implements ITemplateService {

    private final TemplateRepository templateRepository;

    /**
     * createImpl
     */
    @Override
    public Long createTemplate(TemplateCreator creator) {
        Optional<Template> template = EntityOperations.doCreate(templateRepository)
            .create(() -> TemplateMapper.INSTANCE.dtoToEntity(creator))
            .update(Template::init)
            .execute();
        return template.isPresent() ? template.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateTemplate(TemplateUpdater updater) {
        EntityOperations.doUpdate(templateRepository)
            .loadById(updater.getId())
            .update(updater::updateTemplate)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validTemplate(Long id) {
        EntityOperations.doUpdate(templateRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidTemplate(Long id) {
        EntityOperations.doUpdate(templateRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public TemplateVO findById(Long id) {
        Optional<Template> template = templateRepository.findById(id);
        return new TemplateVO(
            template.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<TemplateVO> findByPage(PageRequestWrapper<TemplateQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return templateRepository.findAll(pageRequest).map(TemplateVO::new);
    }
}

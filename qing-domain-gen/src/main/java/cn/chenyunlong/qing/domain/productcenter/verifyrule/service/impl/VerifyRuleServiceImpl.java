package cn.chenyunlong.qing.domain.productcenter.verifyrule.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.VerifyRule;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.creator.VerifyRuleCreator;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.query.VerifyRuleQuery;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.updater.VerifyRuleUpdater;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.vo.VerifyRuleVO;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.mapper.VerifyRuleMapper;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.repository.VerifyRuleRepository;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.service.IVerifyRuleService;
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
public class VerifyRuleServiceImpl implements IVerifyRuleService {

    private final VerifyRuleRepository verifyRuleRepository;

    /**
     * createImpl
     */
    @Override
    public Long createVerifyRule(VerifyRuleCreator creator) {
        Optional<VerifyRule> verifyRule = EntityOperations.doCreate(verifyRuleRepository)
            .create(() -> VerifyRuleMapper.INSTANCE.dtoToEntity(creator))
            .update(VerifyRule::init)
            .execute();
        return verifyRule.isPresent() ? verifyRule.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateVerifyRule(VerifyRuleUpdater updater) {
        EntityOperations.doUpdate(verifyRuleRepository)
            .loadById(updater.getId())
            .update(updater::updateVerifyRule)
            .execute();
    }

    @Override
    public void validVerifyRule(Long id) {
        EntityOperations.doUpdate(verifyRuleRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    @Override
    public void invalidVerifyRule(Long id) {
        EntityOperations.doUpdate(verifyRuleRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public VerifyRuleVO findById(Long id) {
        Optional<VerifyRule> verifyRule = verifyRuleRepository.findById(id);
        return new VerifyRuleVO(
            verifyRule.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<VerifyRuleVO> findByPage(PageRequestWrapper<VerifyRuleQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return verifyRuleRepository.findAll(pageRequest).map(VerifyRuleVO::new);
    }
}

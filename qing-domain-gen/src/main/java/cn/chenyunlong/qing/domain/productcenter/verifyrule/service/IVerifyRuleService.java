package cn.chenyunlong.qing.domain.productcenter.verifyrule.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.creator.VerifyRuleCreator;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.query.VerifyRuleQuery;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.updater.VerifyRuleUpdater;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.vo.VerifyRuleVO;
import org.springframework.data.domain.Page;

public interface IVerifyRuleService {

    /**
     * create
     */
    Long createVerifyRule(VerifyRuleCreator creator);

    /**
     * update
     */
    void updateVerifyRule(VerifyRuleUpdater updater);

    void validVerifyRule(Long id);

    void invalidVerifyRule(Long id);

    /**
     * findById
     */
    VerifyRuleVO findById(Long id);

    /**
     * findByPage
     */
    Page<VerifyRuleVO> findByPage(PageRequestWrapper<VerifyRuleQuery> query);
}

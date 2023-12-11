package cn.chenyunlong.qing.domain.productcenter.verifyrule.mapper;

import cn.chenyunlong.qing.domain.productcenter.verifyrule.VerifyRule;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.creator.VerifyRuleCreator;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.query.VerifyRuleQuery;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.response.VerifyRuleResponse;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.updater.VerifyRuleUpdater;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.vo.VerifyRuleVO;
import cn.hutool.core.bean.BeanUtil;

public interface VerifyRuleMapper {
    VerifyRuleMapper INSTANCE = new VerifyRuleMapper() {
    };

    default VerifyRule dtoToEntity(VerifyRuleCreator dto) {
        return BeanUtil.copyProperties(dto, VerifyRule.class);
    }

    default VerifyRuleUpdater request2Updater(VerifyRuleUpdateRequest request) {
        return BeanUtil.copyProperties(request, VerifyRuleUpdater.class);
    }

    default VerifyRuleCreator request2Dto(VerifyRuleCreateRequest request) {
        return BeanUtil.copyProperties(request, VerifyRuleCreator.class);
    }

    default VerifyRuleQuery request2Query(VerifyRuleQueryRequest request) {
        return BeanUtil.copyProperties(request, VerifyRuleQuery.class);
    }

    default VerifyRuleResponse vo2Response(VerifyRuleVO vo) {
        return BeanUtil.copyProperties(vo, VerifyRuleResponse.class);
    }

    default VerifyRuleResponse vo2CustomResponse(VerifyRuleVO vo) {
        return vo2Response(vo);
    }
}

package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.qing.auth.domain.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.query.QingUserQuery;
import cn.chenyunlong.qing.auth.domain.user.dto.request.QingUserCreateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.QingUserQueryRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.response.QingUserResponse;
import cn.chenyunlong.qing.auth.domain.user.dto.vo.QingUserVO;
import cn.hutool.core.bean.BeanUtil;

public interface QingUserMapper {

    default QingUserCreator request2Dto(QingUserCreateRequest request) {
        return BeanUtil.copyProperties(request, QingUserCreator.class);
    }

    default QingUserQuery request2Query(QingUserQueryRequest request) {
        return BeanUtil.copyProperties(request, QingUserQuery.class);
    }

    default QingUserResponse vo2Response(QingUserVO vo) {
        return BeanUtil.copyProperties(vo, QingUserResponse.class);
    }

    default QingUserResponse vo2CustomResponse(QingUserVO vo) {
        return vo2Response(vo);
    }
}

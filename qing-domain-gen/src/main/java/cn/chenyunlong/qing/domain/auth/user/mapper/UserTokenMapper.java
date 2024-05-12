package cn.chenyunlong.qing.domain.auth.user.mapper;

import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserTokenQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserTokenResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserTokenVO;
import cn.hutool.core.bean.BeanUtil;

public interface UserTokenMapper {

    UserTokenMapper INSTANCE = new UserTokenMapper() {

    };

    default UserToken dtoToEntity(UserTokenCreator dto) {
        return BeanUtil.copyProperties(dto, UserToken.class);
    }

    default UserTokenUpdater request2Updater(UserTokenUpdateRequest request) {
        return BeanUtil.copyProperties(request, UserTokenUpdater.class);
    }

    default UserTokenCreator request2Dto(UserTokenCreateRequest request) {
        return BeanUtil.copyProperties(request, UserTokenCreator.class);
    }

    default UserTokenQuery request2Query(UserTokenQueryRequest request) {
        return BeanUtil.copyProperties(request, UserTokenQuery.class);
    }

    default UserTokenResponse vo2Response(UserTokenVO vo) {
        return BeanUtil.copyProperties(vo, UserTokenResponse.class);
    }

    default UserTokenResponse vo2CustomResponse(UserTokenVO vo) {
        return vo2Response(vo);
    }
}

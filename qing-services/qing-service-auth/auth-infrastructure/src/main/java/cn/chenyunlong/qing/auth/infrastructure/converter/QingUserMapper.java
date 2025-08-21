package cn.chenyunlong.qing.infrastructure.auth.converter;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.query.QingUserQuery;
import cn.chenyunlong.qing.auth.domain.user.dto.request.QingUserCreateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.QingUserQueryRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.QingUserUpdateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.response.QingUserResponse;
import cn.chenyunlong.qing.auth.domain.user.dto.updater.QingUserUpdater;
import cn.chenyunlong.qing.auth.domain.user.dto.vo.QingUserVO;
import cn.hutool.core.bean.BeanUtil;

public interface QingUserMapper {

    QingUserMapper INSTANCE = new QingUserMapper() {

    };

    default QingUser dtoToEntity(QingUserCreator dto) {
        return BeanUtil.copyProperties(dto, QingUser.class);
    }

    default QingUserUpdater request2Updater(QingUserUpdateRequest request) {
        return BeanUtil.copyProperties(request, QingUserUpdater.class);
    }

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

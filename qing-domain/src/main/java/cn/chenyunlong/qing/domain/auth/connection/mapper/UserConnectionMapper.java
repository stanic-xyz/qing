package cn.chenyunlong.qing.domain.auth.connection.mapper;

import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.query.UserConnectionQuery;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionCreateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionQueryRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionUpdateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.response.UserConnectionResponse;
import cn.chenyunlong.qing.domain.auth.connection.dto.updater.UserConnectionUpdater;
import cn.chenyunlong.qing.domain.auth.connection.dto.vo.UserConnectionVO;
import cn.chenyunlong.security.entity.ConnectionData;
import cn.hutool.core.bean.BeanUtil;

public interface UserConnectionMapper {
    UserConnectionMapper INSTANCE = new UserConnectionMapper() {
    };

    default UserConnection dtoToEntity(UserConnectionCreator dto) {
        return BeanUtil.copyProperties(dto, UserConnection.class);
    }

    default UserConnectionUpdater request2Updater(UserConnectionUpdateRequest request) {
        return BeanUtil.copyProperties(request, UserConnectionUpdater.class);
    }

    default UserConnectionCreator request2Dto(UserConnectionCreateRequest request) {
        return BeanUtil.copyProperties(request, UserConnectionCreator.class);
    }

    default UserConnectionQuery request2Query(UserConnectionQueryRequest request) {
        return BeanUtil.copyProperties(request, UserConnectionQuery.class);
    }

    default ConnectionData entityToConnectionData(UserConnection userConnection) {
        return ConnectionData.builder()
                .providerId(userConnection.getProviderId())
                .userId(userConnection.getUserId())
                .accessToken(userConnection.getAccessToken())
                .expireTime(userConnection.getExpireTime())
                .build();
    }

    default UserConnectionResponse vo2Response(UserConnectionVO vo) {
        return BeanUtil.copyProperties(vo, UserConnectionResponse.class);
    }

    default UserConnectionResponse vo2CustomResponse(UserConnectionVO vo) {
        return vo2Response(vo);
    }
}

package cn.chenyunlong.qing.domain.auth.user.mapper;

import cn.chenyunlong.qing.domain.auth.user.User;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import cn.hutool.core.bean.BeanUtil;

public interface UserMapper {
    UserMapper INSTANCE = new UserMapper() {
    };

    default User dtoToEntity(UserCreator dto) {
        return BeanUtil.copyProperties(dto, User.class);
    }

    default UserUpdater request2Updater(UserUpdateRequest request) {
        return BeanUtil.copyProperties(request, UserUpdater.class);
    }

    default UserCreator request2Dto(UserCreateRequest request) {
        return BeanUtil.copyProperties(request, UserCreator.class);
    }

    default UserQuery request2Query(UserQueryRequest request) {
        return BeanUtil.copyProperties(request, UserQuery.class);
    }

    default UserResponse vo2CustomResponse(UserVO vo) {
        return vo2Response(vo);
    }

    default UserResponse vo2Response(UserVO vo) {
        return BeanUtil.copyProperties(vo, UserResponse.class);
    }
}

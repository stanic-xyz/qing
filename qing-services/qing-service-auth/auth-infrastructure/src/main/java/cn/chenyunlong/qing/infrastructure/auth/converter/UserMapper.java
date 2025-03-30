package cn.chenyunlong.qing.infrastructure.auth.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.query.UserQuery;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserCreateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserQueryRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserUpdateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.response.UserResponse;
import cn.chenyunlong.qing.auth.domain.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.auth.domain.user.dto.vo.UserVO;
import cn.chenyunlong.qing.infrastructure.auth.repository.jpa.entity.QingUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    GenericEnumMapper.class,
    DateMapper.class,
    CustomMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    QingUser dtoToEntity(UserCreator dto);

    UserUpdater request2Updater(UserUpdateRequest request);

    @Mapping(source = "password.password", target = "password")
    UserCreator request2Dto(UserCreateRequest request);

    UserQuery request2Query(UserQueryRequest request);

    UserResponse vo2CustomResponse(UserVO vo);

    UserResponse vo2Response(UserVO vo);

    QingUser entityToDomain(QingUserEntity userEntity);
}

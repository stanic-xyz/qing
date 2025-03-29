package cn.chenyunlong.qing.domain.auth.user.mapper;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserTokenQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserTokenResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserTokenVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    GenericEnumMapper.class,
    DateMapper.class,
    CustomMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserTokenMapper {

    UserTokenMapper INSTANCE = Mappers.getMapper(UserTokenMapper.class);

    UserToken dtoToEntity(UserTokenCreator dto);

    UserTokenUpdater request2Updater(UserTokenUpdateRequest request);

    UserTokenCreator request2Dto(UserTokenCreateRequest request);

    UserTokenQuery request2Query(UserTokenQueryRequest request);

    UserTokenResponse vo2Response(UserTokenVO vo);

    UserTokenResponse vo2CustomResponse(UserTokenVO vo);

    UserTokenVO entity2Vo(UserToken userToken);
}

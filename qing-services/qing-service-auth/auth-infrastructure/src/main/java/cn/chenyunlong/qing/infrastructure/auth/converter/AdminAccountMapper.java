package cn.chenyunlong.qing.infrastructure.auth.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.auth.domain.admin.AdminAccount;
import cn.chenyunlong.qing.auth.domain.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.auth.domain.admin.dto.query.AdminAccountQuery;
import cn.chenyunlong.qing.auth.domain.admin.dto.request.AdminAccountCreateRequest;
import cn.chenyunlong.qing.auth.domain.admin.dto.request.AdminAccountQueryRequest;
import cn.chenyunlong.qing.auth.domain.admin.dto.request.AdminAccountUpdateRequest;
import cn.chenyunlong.qing.auth.domain.admin.dto.response.AdminAccountResponse;
import cn.chenyunlong.qing.auth.domain.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.auth.domain.admin.dto.vo.AdminAccountVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    CustomMapper.class,
    DateMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminAccountMapper {

    AdminAccountMapper INSTANCE = Mappers.getMapper(AdminAccountMapper.class);

    AdminAccount dtoToEntity(AdminAccountCreator dto);

    AdminAccountUpdater request2Updater(AdminAccountUpdateRequest request);

    AdminAccountCreator request2Dto(AdminAccountCreateRequest request);

    AdminAccountQuery request2Query(AdminAccountQueryRequest request);

    AdminAccountResponse vo2Response(AdminAccountVO vo);

    AdminAccountResponse vo2CustomResponse(AdminAccountVO vo);
}

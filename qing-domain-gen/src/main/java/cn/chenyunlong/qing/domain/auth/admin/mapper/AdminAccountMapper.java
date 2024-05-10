package cn.chenyunlong.qing.domain.auth.admin.mapper;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.query.AdminAccountQuery;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountCreateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountQueryRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountUpdateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.response.AdminAccountResponse;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.domain.auth.admin.dto.vo.AdminAccountVO;
import cn.hutool.core.bean.BeanUtil;

public interface AdminAccountMapper {

    AdminAccountMapper INSTANCE = new AdminAccountMapper() {

    };

    default AdminAccount dtoToEntity(AdminAccountCreator dto) {
        return BeanUtil.copyProperties(dto, AdminAccount.class);
    }

    default AdminAccountUpdater request2Updater(AdminAccountUpdateRequest request) {
        return BeanUtil.copyProperties(request, AdminAccountUpdater.class);
    }

    default AdminAccountCreator request2Dto(AdminAccountCreateRequest request) {
        return BeanUtil.copyProperties(request, AdminAccountCreator.class);
    }

    default AdminAccountQuery request2Query(AdminAccountQueryRequest request) {
        return BeanUtil.copyProperties(request, AdminAccountQuery.class);
    }

    default AdminAccountResponse vo2Response(AdminAccountVO vo) {
        return BeanUtil.copyProperties(vo, AdminAccountResponse.class);
    }

    default AdminAccountResponse vo2CustomResponse(AdminAccountVO vo) {
        return vo2Response(vo);
    }
}

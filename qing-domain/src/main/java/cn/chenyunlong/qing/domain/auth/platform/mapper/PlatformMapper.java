package cn.chenyunlong.qing.domain.auth.platform.mapper;

import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.query.PlatformQuery;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformCreateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformQueryRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformUpdateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.response.PlatformResponse;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;
import cn.hutool.core.bean.BeanUtil;

public interface PlatformMapper {
    PlatformMapper INSTANCE = new PlatformMapper() {
    };

    default Platform dtoToEntity(PlatformCreator dto) {
        return BeanUtil.copyProperties(dto, Platform.class);
    }

    default PlatformUpdater request2Updater(PlatformUpdateRequest request) {
        return BeanUtil.copyProperties(request, PlatformUpdater.class);
    }

    default PlatformCreator request2Dto(PlatformCreateRequest request) {
        return BeanUtil.copyProperties(request, PlatformCreator.class);
    }

    default PlatformQuery request2Query(PlatformQueryRequest request) {
        return BeanUtil.copyProperties(request, PlatformQuery.class);
    }

    default PlatformResponse vo2Response(PlatformVO vo) {
        return BeanUtil.copyProperties(vo, PlatformResponse.class);
    }

    default PlatformResponse vo2CustomResponse(PlatformVO vo) {
        return vo2Response(vo);
    }
}

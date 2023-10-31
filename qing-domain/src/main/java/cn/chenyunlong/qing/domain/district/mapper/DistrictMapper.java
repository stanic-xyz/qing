package cn.chenyunlong.qing.domain.district.mapper;

import cn.chenyunlong.qing.domain.district.District;
import cn.chenyunlong.qing.domain.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.district.dto.query.DistrictQuery;
import cn.chenyunlong.qing.domain.district.dto.request.DistrictCreateRequest;
import cn.chenyunlong.qing.domain.district.dto.request.DistrictQueryRequest;
import cn.chenyunlong.qing.domain.district.dto.request.DistrictUpdateRequest;
import cn.chenyunlong.qing.domain.district.dto.response.DistrictResponse;
import cn.chenyunlong.qing.domain.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.district.dto.vo.DistrictVO;
import cn.hutool.core.bean.BeanUtil;

public interface DistrictMapper {
    DistrictMapper INSTANCE = new DistrictMapper() {
    };

    default District dtoToEntity(DistrictCreator dto) {
        return BeanUtil.copyProperties(dto, District.class);
    }

    default DistrictUpdater request2Updater(DistrictUpdateRequest request) {
        return BeanUtil.copyProperties(request, DistrictUpdater.class);
    }

    default DistrictCreator request2Dto(DistrictCreateRequest request) {
        return BeanUtil.copyProperties(request, DistrictCreator.class);
    }

    default DistrictQuery request2Query(DistrictQueryRequest request) {
        return BeanUtil.copyProperties(request, DistrictQuery.class);
    }

    default DistrictResponse vo2CustomResponse(DistrictVO vo) {
        return vo2Response(vo);
    }

    default DistrictResponse vo2Response(DistrictVO vo) {
        return BeanUtil.copyProperties(vo, DistrictResponse.class);
    }
}

package cn.chenyunlong.qing.domain.zan.mapper;

import cn.chenyunlong.qing.domain.zan.Zan;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.query.ZanQuery;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanQueryRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanUpdateRequest;
import cn.chenyunlong.qing.domain.zan.dto.response.ZanResponse;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.dto.vo.ZanVO;
import cn.hutool.core.bean.BeanUtil;

public interface ZanMapper {
    ZanMapper INSTANCE = new ZanMapper() {
    };

    default Zan dtoToEntity(ZanCreator dto) {
        return BeanUtil.copyProperties(dto, Zan.class);
    }

    default ZanUpdater request2Updater(ZanUpdateRequest request) {
        return BeanUtil.copyProperties(request, ZanUpdater.class);
    }

    default ZanCreator request2Dto(ZanCreateRequest request) {
        return BeanUtil.copyProperties(request, ZanCreator.class);
    }

    default ZanQuery request2Query(ZanQueryRequest request) {
        return BeanUtil.copyProperties(request, ZanQuery.class);
    }

    default ZanResponse vo2CustomResponse(ZanVO vo) {
        return vo2Response(vo);
    }

    default ZanResponse vo2Response(ZanVO vo) {
        return BeanUtil.copyProperties(vo, ZanResponse.class);
    }
}

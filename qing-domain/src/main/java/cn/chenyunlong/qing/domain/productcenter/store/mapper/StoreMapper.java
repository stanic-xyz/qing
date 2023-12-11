package cn.chenyunlong.qing.domain.productcenter.store.mapper;

import cn.chenyunlong.qing.domain.productcenter.store.Store;
import cn.chenyunlong.qing.domain.productcenter.store.dto.creator.StoreCreator;
import cn.chenyunlong.qing.domain.productcenter.store.dto.query.StoreQuery;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.response.StoreResponse;
import cn.chenyunlong.qing.domain.productcenter.store.dto.updater.StoreUpdater;
import cn.chenyunlong.qing.domain.productcenter.store.dto.vo.StoreVO;
import cn.hutool.core.bean.BeanUtil;

public interface StoreMapper {
    StoreMapper INSTANCE = new StoreMapper() {
    };

    default Store dtoToEntity(StoreCreator dto) {
        return BeanUtil.copyProperties(dto, Store.class);
    }

    default StoreUpdater request2Updater(StoreUpdateRequest request) {
        return BeanUtil.copyProperties(request, StoreUpdater.class);
    }

    default StoreCreator request2Dto(StoreCreateRequest request) {
        return BeanUtil.copyProperties(request, StoreCreator.class);
    }

    default StoreQuery request2Query(StoreQueryRequest request) {
        return BeanUtil.copyProperties(request, StoreQuery.class);
    }

    default StoreResponse vo2Response(StoreVO vo) {
        return BeanUtil.copyProperties(vo, StoreResponse.class);
    }

    default StoreResponse vo2CustomResponse(StoreVO vo) {
        return vo2Response(vo);
    }
}

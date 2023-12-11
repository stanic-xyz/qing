package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.mapper;

import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.GoodsLifeCycle;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.creator.GoodsLifeCycleCreator;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.query.GoodsLifeCycleQuery;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.response.GoodsLifeCycleResponse;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.updater.GoodsLifeCycleUpdater;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.vo.GoodsLifeCycleVO;
import cn.hutool.core.bean.BeanUtil;

public interface GoodsLifeCycleMapper {
    GoodsLifeCycleMapper INSTANCE = new GoodsLifeCycleMapper() {
    };

    default GoodsLifeCycle dtoToEntity(GoodsLifeCycleCreator dto) {
        return BeanUtil.copyProperties(dto, GoodsLifeCycle.class);
    }

    default GoodsLifeCycleUpdater request2Updater(GoodsLifeCycleUpdateRequest request) {
        return BeanUtil.copyProperties(request, GoodsLifeCycleUpdater.class);
    }

    default GoodsLifeCycleCreator request2Dto(GoodsLifeCycleCreateRequest request) {
        return BeanUtil.copyProperties(request, GoodsLifeCycleCreator.class);
    }

    default GoodsLifeCycleQuery request2Query(GoodsLifeCycleQueryRequest request) {
        return BeanUtil.copyProperties(request, GoodsLifeCycleQuery.class);
    }

    default GoodsLifeCycleResponse vo2Response(GoodsLifeCycleVO vo) {
        return BeanUtil.copyProperties(vo, GoodsLifeCycleResponse.class);
    }

    default GoodsLifeCycleResponse vo2CustomResponse(GoodsLifeCycleVO vo) {
        return vo2Response(vo);
    }
}

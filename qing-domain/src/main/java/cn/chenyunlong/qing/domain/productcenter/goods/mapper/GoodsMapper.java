package cn.chenyunlong.qing.domain.productcenter.goods.mapper;

import cn.chenyunlong.qing.domain.productcenter.goods.Goods;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.creator.GoodsCreator;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.query.GoodsQuery;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.response.GoodsResponse;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.updater.GoodsUpdater;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.vo.GoodsVO;
import cn.hutool.core.bean.BeanUtil;

public interface GoodsMapper {
    GoodsMapper INSTANCE = new GoodsMapper() {
    };

    default Goods dtoToEntity(GoodsCreator dto) {
        return BeanUtil.copyProperties(dto, Goods.class);
    }

    default GoodsUpdater request2Updater(GoodsUpdateRequest request) {
        return BeanUtil.copyProperties(request, GoodsUpdater.class);
    }

    default GoodsCreator request2Dto(GoodsCreateRequest request) {
        return BeanUtil.copyProperties(request, GoodsCreator.class);
    }

    default GoodsQuery request2Query(GoodsQueryRequest request) {
        return BeanUtil.copyProperties(request, GoodsQuery.class);
    }

    default GoodsResponse vo2Response(GoodsVO vo) {
        return BeanUtil.copyProperties(vo, GoodsResponse.class);
    }

    default GoodsResponse vo2CustomResponse(GoodsVO vo) {
        return vo2Response(vo);
    }
}

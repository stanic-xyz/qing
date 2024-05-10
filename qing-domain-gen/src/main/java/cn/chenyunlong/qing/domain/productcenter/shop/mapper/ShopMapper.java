package cn.chenyunlong.qing.domain.productcenter.shop.mapper;

import cn.chenyunlong.qing.domain.productcenter.shop.Shop;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.creator.ShopCreator;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.query.ShopQuery;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.response.ShopResponse;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.updater.ShopUpdater;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.vo.ShopVO;
import cn.hutool.core.bean.BeanUtil;

public interface ShopMapper {

    ShopMapper INSTANCE = new ShopMapper() {

    };

    default Shop dtoToEntity(ShopCreator dto) {
        return BeanUtil.copyProperties(dto, Shop.class);
    }

    default ShopUpdater request2Updater(ShopUpdateRequest request) {
        return BeanUtil.copyProperties(request, ShopUpdater.class);
    }

    default ShopCreator request2Dto(ShopCreateRequest request) {
        return BeanUtil.copyProperties(request, ShopCreator.class);
    }

    default ShopQuery request2Query(ShopQueryRequest request) {
        return BeanUtil.copyProperties(request, ShopQuery.class);
    }

    default ShopResponse vo2Response(ShopVO vo) {
        return BeanUtil.copyProperties(vo, ShopResponse.class);
    }

    default ShopResponse vo2CustomResponse(ShopVO vo) {
        return vo2Response(vo);
    }
}

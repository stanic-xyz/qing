package cn.chenyunlong.qing.domain.anime.faverate.mapper;

import cn.chenyunlong.qing.domain.anime.faverate.Favorite;
import cn.chenyunlong.qing.domain.anime.faverate.dto.creator.FavoriteCreator;
import cn.chenyunlong.qing.domain.anime.faverate.dto.query.FavoriteQuery;
import cn.chenyunlong.qing.domain.anime.faverate.dto.request.FavoriteCreateRequest;
import cn.chenyunlong.qing.domain.anime.faverate.dto.request.FavoriteQueryRequest;
import cn.chenyunlong.qing.domain.anime.faverate.dto.request.FavoriteUpdateRequest;
import cn.chenyunlong.qing.domain.anime.faverate.dto.response.FavoriteResponse;
import cn.chenyunlong.qing.domain.anime.faverate.dto.updater.FavoriteUpdater;
import cn.chenyunlong.qing.domain.anime.faverate.dto.vo.FavoriteVO;
import cn.hutool.core.bean.BeanUtil;

public interface FavoriteMapper {

    FavoriteMapper INSTANCE = new FavoriteMapper() {

    };

    default Favorite dtoToEntity(FavoriteCreator dto) {
        return BeanUtil.copyProperties(dto, Favorite.class);
    }

    default FavoriteUpdater request2Updater(FavoriteUpdateRequest request) {
        return BeanUtil.copyProperties(request, FavoriteUpdater.class);
    }

    default FavoriteCreator request2Dto(FavoriteCreateRequest request) {
        return BeanUtil.copyProperties(request, FavoriteCreator.class);
    }

    default FavoriteQuery request2Query(FavoriteQueryRequest request) {
        return BeanUtil.copyProperties(request, FavoriteQuery.class);
    }

    default FavoriteResponse vo2Response(FavoriteVO vo) {
        return BeanUtil.copyProperties(vo, FavoriteResponse.class);
    }

    default FavoriteResponse vo2CustomResponse(FavoriteVO vo) {
        return vo2Response(vo);
    }
}

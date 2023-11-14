package cn.chenyunlong.qing.domain.anime.mapper;

import cn.chenyunlong.qing.domain.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.domain.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.domain.anime.dto.request.AnimeCategoryQueryRequest;
import cn.chenyunlong.qing.domain.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.anime.dto.response.AnimeCategoryResponse;
import cn.chenyunlong.qing.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.dto.vo.AnimeCategoryVO;
import cn.hutool.core.bean.BeanUtil;

public interface AnimeCategoryMapper {
    AnimeCategoryMapper INSTANCE = new AnimeCategoryMapper() {
    };

    default AnimeCategory dtoToEntity(AnimeCategoryCreator dto) {
        return BeanUtil.copyProperties(dto, AnimeCategory.class);
    }

    default AnimeCategoryUpdater request2Updater(AnimeCategoryUpdateRequest request) {
        return BeanUtil.copyProperties(request, AnimeCategoryUpdater.class);
    }

    default AnimeCategoryCreator request2Dto(AnimeCategoryCreateRequest request) {
        return BeanUtil.copyProperties(request, AnimeCategoryCreator.class);
    }

    default AnimeCategoryQuery request2Query(AnimeCategoryQueryRequest request) {
        return BeanUtil.copyProperties(request, AnimeCategoryQuery.class);
    }

    default AnimeCategoryResponse vo2Response(AnimeCategoryVO vo) {
        return BeanUtil.copyProperties(vo, AnimeCategoryResponse.class);
    }

    default AnimeCategoryResponse vo2CustomResponse(AnimeCategoryVO vo) {
        return vo2Response(vo);
    }
}

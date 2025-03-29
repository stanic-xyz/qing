package cn.chenyunlong.qing.domain.auth.menu.converter;


import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.anime.Category;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeCategoryResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    Category dtoToEntity(AnimeCategoryCreator dto);

    AnimeCategoryUpdater request2Updater(AnimeCategoryUpdateRequest request);

    AnimeCategoryCreator request2Dto(AnimeCategoryCreateRequest request);

    AnimeCategoryQuery request2Query(AnimeCategoryQueryRequest request);

    AnimeCategoryResponse vo2Response(AnimeCategoryVO vo);

    AnimeCategoryResponse vo2CustomResponse(AnimeCategoryVO vo);

    AnimeCategoryVO entityToVo(Category category);
}

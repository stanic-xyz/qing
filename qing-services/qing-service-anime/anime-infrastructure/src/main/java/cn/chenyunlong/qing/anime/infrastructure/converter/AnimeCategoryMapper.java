package cn.chenyunlong.qing.anime.infrastructure.converter;


import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeCategoryCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryQueryRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.response.AnimeCategoryResponse;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryTreeVO;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.CategoryEntity;
import cn.chenyunlong.qing.domain.common.converter.AggregateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnimeCategoryMapper {

    AnimeCategoryMapper INSTANCE = Mappers.getMapper(AnimeCategoryMapper.class);

    Category dtoToEntity(AnimeCategoryCreator dto);

    Category entityToEntity(CategoryEntity entity);

    AnimeCategoryUpdater request2Updater(AnimeCategoryUpdateRequest request);

    AnimeCategoryCreator request2Dto(AnimeCategoryCreateRequest request);

    AnimeCategoryQuery request2Query(AnimeCategoryQueryRequest request);

    AnimeCategoryResponse vo2Response(AnimeCategoryVO vo);

    AnimeCategoryResponse vo2CustomResponse(AnimeCategoryVO vo);

    AnimeCategoryVO entityToVo(Category category);

    AnimeCategoryTreeVO entityToTreeVo(CategoryEntity categoryEntity);

    CategoryEntity domainToEntity(Category domain);
}

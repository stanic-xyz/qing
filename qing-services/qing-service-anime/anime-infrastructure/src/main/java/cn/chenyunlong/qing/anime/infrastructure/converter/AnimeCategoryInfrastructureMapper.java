package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeCategoryCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.response.AnimeCategoryResponse;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryTreeVO;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.anime.domain.anime.models.CategoryId;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomMapper.class, DateMapper.class,
        AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnimeCategoryInfrastructureMapper {

    Category dtoToEntity(AnimeCategoryCreator dto);

    Category entityToEntity(CategoryEntity entity);

    AnimeCategoryUpdater request2Updater(AnimeCategoryUpdateRequest request);

    AnimeCategoryCreator request2Dto(AnimeCategoryCreateRequest request);

    AnimeCategoryResponse vo2CustomResponse(AnimeCategoryVO vo);


    AnimeCategoryTreeVO entityToTreeVo(CategoryEntity categoryEntity);

    CategoryEntity domainToEntity(Category domain);

    default CategoryId toCategoryId(Long value) {
        return CategoryId.of(value);
    }
}

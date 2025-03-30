package cn.chenyunlong.qing.infrastructure.auth.converter;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.auth.domain.menu.dto.query.SysMenuQuery;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuQueryRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.response.SysMenuResponse;
import cn.chenyunlong.qing.auth.domain.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.auth.domain.menu.dto.vo.SysMenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    DateMapper.class,
    GenericEnumMapper.class
})
public interface SysMenuMapper {

    SysMenuMapper INSTANCE = Mappers.getMapper(SysMenuMapper.class);

    SysMenu dtoToEntity(SysMenuCreator dto);

    SysMenuUpdater request2Updater(SysMenuUpdateRequest request);

    SysMenuCreator request2Dto(SysMenuCreateRequest request);

    SysMenuQuery request2Query(SysMenuQueryRequest request);

    SysMenuResponse vo2Response(SysMenuVO vo);

    SysMenuVO entityToVO(SysMenu sysMenu);

}

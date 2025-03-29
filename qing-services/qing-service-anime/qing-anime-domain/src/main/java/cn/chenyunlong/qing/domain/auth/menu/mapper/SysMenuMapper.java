package cn.chenyunlong.qing.domain.auth.menu.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import cn.chenyunlong.qing.domain.auth.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.domain.auth.menu.dto.query.SysMenuQuery;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuQueryRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.domain.auth.menu.dto.response.SysMenuResponse;
import cn.chenyunlong.qing.domain.auth.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
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

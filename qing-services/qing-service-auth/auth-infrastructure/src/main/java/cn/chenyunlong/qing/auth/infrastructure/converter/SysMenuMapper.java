package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.SysMenuId;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.CreateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.UpdateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuCreateRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.request.SysMenuUpdateRequest;
import cn.chenyunlong.qing.auth.domain.menu.dto.response.SysMenuResponse;
import cn.chenyunlong.qing.auth.domain.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.auth.infrastructure.converter.base.GenericEnumMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.MenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(uses = {
        DateMapper.class,
        GenericEnumMapper.class,
        AggregateMapper.class
}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysMenuMapper {

    SysMenu dtoToEntity(CreateSysMenuCommand dto);

    UpdateSysMenuCommand request2Updater(SysMenuUpdateRequest request);

    CreateSysMenuCommand request2Dto(SysMenuCreateRequest request);

    SysMenuResponse vo2Response(SysMenuVO vo);

    List<SysMenuVO> entity2Vo(List<MenuEntity> menuEntityList);

    /**
     * 将领域模型对象(Domain)转换为实体对象(Entity)
     *
     * @return SysMenu 返回转换后的实体对象
     */
    MenuEntity domain2Entity(SysMenu domain);

    SysMenu entity2Domain(MenuEntity menuEntity);

    default SysMenuId mapId(Long value) {
        return SysMenuId.of(value);
    }
}

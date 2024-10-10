package cn.chenyunlong.qing.domain.auth.menu.dto.updater;

import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Schema
@Data
public class SysMenuUpdater {

    @Schema(
        title = "menuId",
        description = "menuId"
    )
    private Long menuId;

    @Schema(
        title = "menuName",
        description = "menuName"
    )
    private String menuName;

    @Schema(
        title = "parentName",
        description = "parentName"
    )
    private String parentName;

    @Schema(
        title = "parentId",
        description = "parentId"
    )
    private Long parentId;

    @Schema(
        title = "orderNum",
        description = "orderNum"
    )
    private Integer orderNum;

    @Schema(
        title = "path",
        description = "path"
    )
    private String path;

    @Schema(
        title = "component",
        description = "component"
    )
    private String component;

    @Schema(
        title = "query",
        description = "query"
    )
    private String query;

    @Schema(
        title = "isFrame",
        description = "isFrame"
    )
    private String isFrame;

    @Schema(
        title = "isCache",
        description = "isCache"
    )
    private String isCache;

    @Schema(
        title = "menuType",
        description = "menuType"
    )
    private String menuType;

    @Schema(
        title = "visible",
        description = "visible"
    )
    private String visible;

    @Schema(
        title = "status",
        description = "status"
    )
    private String status;

    @Schema(
        title = "perms",
        description = "perms"
    )
    private String perms;

    @Schema(
        title = "icon",
        description = "icon"
    )
    private String icon;

    @Schema(
        title = "children",
        description = "children"
    )
    private List<SysMenu> children;

    private Long id;

    public void updateSysMenu(SysMenu param) {
        Optional.ofNullable(getMenuId()).ifPresent(param::setMenuId);
        Optional.ofNullable(getMenuName()).ifPresent(param::setMenuName);
        Optional.ofNullable(getParentName()).ifPresent(param::setParentName);
        Optional.ofNullable(getParentId()).ifPresent(param::setParentId);
        Optional.ofNullable(getOrderNum()).ifPresent(param::setOrderNum);
        Optional.ofNullable(getPath()).ifPresent(param::setPath);
        Optional.ofNullable(getComponent()).ifPresent(param::setComponent);
        Optional.ofNullable(getQuery()).ifPresent(param::setQuery);
        Optional.ofNullable(getIsFrame()).ifPresent(param::setIsFrame);
        Optional.ofNullable(getIsCache()).ifPresent(param::setIsCache);
        Optional.ofNullable(getMenuType()).ifPresent(param::setMenuType);
        Optional.ofNullable(getVisible()).ifPresent(param::setVisible);
        Optional.ofNullable(getStatus()).ifPresent(param::setStatus);
        Optional.ofNullable(getPerms()).ifPresent(param::setPerms);
        Optional.ofNullable(getIcon()).ifPresent(param::setIcon);
    }

}

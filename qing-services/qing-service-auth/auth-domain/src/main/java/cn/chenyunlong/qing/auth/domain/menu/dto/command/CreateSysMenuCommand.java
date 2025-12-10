package cn.chenyunlong.qing.auth.domain.menu.dto.command;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.dto.enums.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema
@Data
public class CreateSysMenuCommand {

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
    private Boolean isFrame = false;

    @Schema(
            title = "isCache",
            description = "isCache"
    )
    private Boolean isCache = false;

    @Schema(
            title = "menuType",
            description = "menuType"
    )
    private MenuType menuType = MenuType.M;

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
}

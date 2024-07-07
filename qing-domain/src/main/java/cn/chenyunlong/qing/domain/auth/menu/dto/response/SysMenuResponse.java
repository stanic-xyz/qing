package cn.chenyunlong.qing.domain.auth.menu.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class SysMenuResponse extends AbstractJpaResponse {

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
}
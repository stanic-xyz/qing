package cn.chenyunlong.qing.auth.domain.menu;

import cn.chenyunlong.qing.auth.domain.menu.dto.enums.MenuType;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单权限表 sys_menu
 *
 * @author ruoyi
 */
@Getter
@Setter
public class SysMenu extends BaseSimpleBusinessEntity<SysMenuId> {

    /**
     * 菜单标识
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 父菜单标识
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;


    /**
     * 路由地址
     */
    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;


    /**
     * 组件路径。vue组件可能使用
     */
    @Size(max = 200, message = "组件路径不能超过255个字符")
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @NotBlank(message = "菜单类型不能为空")
    private MenuType menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 权限字符串
     */
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    public static SysMenu create(SysMenuId menuId, Long id, String menuName, MenuType menuType, Integer orderNum) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(menuId);
        sysMenu.setMenuId(id);
        sysMenu.setMenuName(menuName);
        sysMenu.setMenuType(menuType);
        sysMenu.setOrderNum(orderNum);
        return sysMenu;
    }
}

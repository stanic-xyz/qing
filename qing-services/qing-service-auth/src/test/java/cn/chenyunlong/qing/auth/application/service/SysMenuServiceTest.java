package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.auth.BaseJpaIntegrationTest;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.CreateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.UpdateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.enums.MenuType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class SysMenuServiceTest extends BaseJpaIntegrationTest {


    @Autowired
    private SysMenuService sysMenuService;

    @Test
    void createSysMenu_success() {
        CreateSysMenuCommand command = new CreateSysMenuCommand();
        command.setMenuId(1001L);
        command.setMenuName("系统管理");
        command.setMenuType(MenuType.M);
        command.setOrderNum(1);

        var result = sysMenuService.createSysMenu(command);

        assertNotNull(result);
        assertEquals("系统管理", result.getMenuName());
        assertEquals(MenuType.M, result.getMenuType());
    }

    @Test
    void createSysMenu_duplicateName_shouldThrowException() {
        CreateSysMenuCommand command1 = new CreateSysMenuCommand();
        command1.setMenuId(1001L);
        command1.setMenuName("用户管理");
        command1.setMenuType(MenuType.M);
        command1.setOrderNum(1);
        sysMenuService.createSysMenu(command1);

        CreateSysMenuCommand command2 = new CreateSysMenuCommand();
        command2.setMenuId(1002L);
        command2.setMenuName("用户管理");
        command2.setMenuType(MenuType.M);
        command2.setOrderNum(2);

        assertThrows(IllegalArgumentException.class, () -> sysMenuService.createSysMenu(command2), "菜单已存在！");
    }

    @Test
    void createSysMenu_withInvalidParentId_shouldThrowException() {
        CreateSysMenuCommand command = new CreateSysMenuCommand();
        command.setMenuId(1001L);
        command.setMenuName("子菜单");
        command.setMenuType(MenuType.C);
        command.setOrderNum(1);
        command.setParentId(9999L); // 不存在的父级ID

        assertThrows(NotFoundException.class, () -> {
            sysMenuService.createSysMenu(command);
        });
    }

    @Test
    void updateSysMenu_success() {
        CreateSysMenuCommand createCommand = new CreateSysMenuCommand();
        createCommand.setMenuId(1001L);
        createCommand.setMenuName("原始菜单");
        createCommand.setMenuType(MenuType.M);
        createCommand.setOrderNum(1);
        var createdMenu = sysMenuService.createSysMenu(createCommand);

        UpdateSysMenuCommand updateCommand = new UpdateSysMenuCommand();
        updateCommand.setId(createdMenu.getId().id());
        updateCommand.setMenuName("更新后的菜单");
        updateCommand.setMenuType(MenuType.C);

        assertDoesNotThrow(() -> {
            sysMenuService.updateSysMenu(updateCommand);
        });
    }

    @Test
    void validAndInvalidSysMenu() {
        CreateSysMenuCommand command = new CreateSysMenuCommand();
        command.setMenuId(1001L);
        command.setMenuName("测试菜单");
        command.setMenuType(MenuType.M);
        command.setOrderNum(1);
        var createdMenu = sysMenuService.createSysMenu(command);

        assertDoesNotThrow(() -> {
            sysMenuService.invalidSysMenu(createdMenu.getId().id());
        });

        assertDoesNotThrow(() -> {
            sysMenuService.validSysMenu(createdMenu.getId().id());
        });
    }

    @Test
    void createSysMenu_withEmptyName_shouldThrowValidationException() {
        CreateSysMenuCommand command = new CreateSysMenuCommand();
        command.setMenuId(1001L);
        command.setMenuName(""); // 空名称
        command.setMenuType(MenuType.M);
        command.setOrderNum(1);

        assertThrows(IllegalArgumentException.class, () -> sysMenuService.createSysMenu(command), "菜单名称不能为空！");
    }
}

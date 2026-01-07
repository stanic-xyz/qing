package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.SysMenuId;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.CreateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.command.UpdateSysMenuCommand;
import cn.chenyunlong.qing.auth.domain.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.auth.domain.menu.event.MenuCreatedEvent;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SysMenuService {

    private final SysMenuRepository sysMenuRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 创建菜单
     */
    public SysMenu createSysMenu(CreateSysMenuCommand command) {
        // 创建菜单实体
        SysMenu menuEntity = SysMenu.create(SysMenuId.next(), command.getMenuId(), command.getMenuName(), command.getMenuType(), command.getOrderNum());

        Assert.notBlank(command.getMenuName(), "菜单名称不能为空！");
        Assert.isFalse(sysMenuRepository.existsByName(command.getMenuName()), "菜单已存在！");

        if (command.getParentId() != null) {
            // TODO 检查父级菜单是否存在
            SysMenu parentMenu = sysMenuRepository.findById(SysMenuId.of(command.getParentId()))
                    .orElseThrow(() -> new NotFoundException("父级菜单不存在"));

            menuEntity.setParentId(parentMenu.getId().id());
            menuEntity.setParentName(parentMenu.getMenuName());
        } else {
            menuEntity.setParentId(null);
            menuEntity.setParentName(null);
        }
        SysMenu savedMenu = sysMenuRepository.save(menuEntity);

        // 创建并发布领域事件
        MenuCreatedEvent event = new MenuCreatedEvent(savedMenu);
        // TODO 发布事件到事件总线
        sysMenuRepository.save(savedMenu);

        eventPublisher.publishEvent(event);
        return savedMenu;
    }

    /**
     * update
     */

    public void updateSysMenu(UpdateSysMenuCommand updater) {
        EntityOperations.doUpdate(sysMenuRepository)
                .loadById(SysMenuId.of(updater.getId()))
                .update(updater::updateSysMenu)
                .execute();
    }


    public void validSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
                .loadById(SysMenuId.of(id))
                .update(SysMenu::valid)
                .execute();
    }


    public void invalidSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
                .loadById(SysMenuId.of(id))
                .update(SysMenu::invalid)
                .execute();
    }

    /**
     * findById
     */
    public SysMenuVO findById(Long id) {
        Optional<SysMenu> sysMenu = sysMenuRepository.findById(SysMenuId.of(id));
        return sysMenu.map(this::entityToVO).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private SysMenuVO entityToVO(SysMenu sysMenu) {
        // TODO 解决这里的问题
        return null;
    }
}

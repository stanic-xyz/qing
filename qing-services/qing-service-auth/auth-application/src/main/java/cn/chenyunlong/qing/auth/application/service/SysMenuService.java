package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.auth.domain.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.auth.domain.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SysMenuService {

    private final SysMenuRepository sysMenuRepository;

    /**
     * createImpl
     */

    public Long createSysMenu(SysMenuCreator creator) {
        Optional<SysMenu> sysMenu = EntityOperations.doCreate(sysMenuRepository)
            .create(() -> {
                SysMenu menuEntity = new SysMenu();
                Optional.ofNullable(creator.getParentId()).ifPresentOrElse(parentId -> {
                    SysMenu parentMenu = sysMenuRepository.findById(new AggregateId(creator.getParentId()))
                        .orElseThrow(() -> new NotFoundException("parent menu not found"));
                    menuEntity.setParentId(parentMenu.getId().getId());
                    menuEntity.setParentName(parentMenu.getMenuName());
                }, () -> {
                    menuEntity.setParentId(null);
                    menuEntity.setParentName(null);
                });
                return menuEntity;
            })
            .update(SysMenu::init)
            .execute();
        return sysMenu.isPresent() ? sysMenu.get().getId().getId() : 0;
    }

    /**
     * update
     */

    public void updateSysMenu(SysMenuUpdater updater) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateSysMenu)
            .execute();
    }


    public void validSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }


    public void invalidSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    public SysMenuVO findById(Long id) {
        Optional<SysMenu> sysMenu = sysMenuRepository.findById(new AggregateId(id));
        return sysMenu.map(this::entityToVO).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private SysMenuVO entityToVO(SysMenu sysMenu) {
        // TODO 解决这里的问题
        return null;
    }

    public List<SysMenuVO> treeMenu() {
        return null;
    }
}

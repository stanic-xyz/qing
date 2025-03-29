package cn.chenyunlong.qing.domain.auth.menu.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import cn.chenyunlong.qing.domain.auth.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.domain.auth.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.domain.auth.menu.mapper.SysMenuMapper;
import cn.chenyunlong.qing.domain.auth.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.domain.auth.menu.service.ISysMenuService;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysMenuRepository sysMenuRepository;

    /**
     * createImpl
     */
    @Override
    public Long createSysMenu(SysMenuCreator creator) {
        Optional<SysMenu> sysMenu = EntityOperations.doCreate(sysMenuRepository)
            .create(() -> {
                SysMenu menuEntity = SysMenuMapper.INSTANCE.dtoToEntity(creator);
                Optional.ofNullable(creator.getParentId()).ifPresentOrElse(parentId -> {
                    SysMenu parentMenu = sysMenuRepository.findById(new AggregateId(creator.getParentId()))
                        .orElseThrow(() -> new NotFoundException("parent menu not found"));
                    menuEntity.setParentId(parentMenu.getAggregateId().getId());
                    menuEntity.setParentName(parentMenu.getMenuName());
                }, () -> {
                    menuEntity.setParentId(null);
                    menuEntity.setParentName(null);
                });
                return menuEntity;
            })
            .update(SysMenu::init)
            .execute();
        return sysMenu.isPresent() ? sysMenu.get().getAggregateId().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateSysMenu(SysMenuUpdater updater) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateSysMenu)
            .execute();
    }

    @Override
    public void validSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public SysMenuVO findById(Long id) {
        Optional<SysMenu> sysMenu = sysMenuRepository.findById(new AggregateId(id));
        return sysMenu.map(SysMenuMapper.INSTANCE::entityToVO).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }
}

package cn.chenyunlong.qing.domain.auth.menu.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import cn.chenyunlong.qing.domain.auth.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.domain.auth.menu.dto.query.SysMenuQuery;
import cn.chenyunlong.qing.domain.auth.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.domain.auth.menu.mapper.SysMenuMapper;
import cn.chenyunlong.qing.domain.auth.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.domain.auth.menu.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
                    SysMenu parentMenu = sysMenuRepository.findById(creator.getParentId())
                        .orElseThrow(() -> new NotFoundException("parent menu not found"));
                    menuEntity.setParentId(parentMenu.getId());
                    menuEntity.setParentName(parentMenu.getMenuName());
                }, () -> {
                    menuEntity.setParentId(null);
                    menuEntity.setParentName(null);
                });
                return menuEntity;
            })
            .update(SysMenu::init)
            .execute();
        return sysMenu.isPresent() ? sysMenu.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateSysMenu(SysMenuUpdater updater) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(updater.getId())
            .update(updater::updateSysMenu)
            .execute();
    }

    @Override
    public void validSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    @Override
    public void invalidSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public SysMenuVO findById(Long id) {
        Optional<SysMenu> sysMenu = sysMenuRepository.findById(id);
        return sysMenu.map(SysMenuMapper.INSTANCE::entityToVO).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    /**
     * findByPage
     */
    @Override
    public Page<SysMenuVO> findByPage(PageRequestWrapper<SysMenuQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return sysMenuRepository.findAll(pageRequest).map(SysMenuMapper.INSTANCE::entityToVO);
    }

    /**
     * findByPage
     */
    @Override
    public List<SysMenuVO> tree() {
        final List<SysMenu> menuList = sysMenuRepository.findAll();
        return menuList.stream()
            .filter(sysMenu -> Objects.isNull(sysMenu.getParentId()))
            .map(sysMenu -> {
                SysMenuVO menuVO = SysMenuMapper.INSTANCE.entityToVO(sysMenu);
                resolveChildMenu(menuVO, menuList);
                return menuVO;
            })
            .toList();
    }

    /**
     * 遍历查询子目录
     */
    private void resolveChildMenu(SysMenuVO parentMenu, List<SysMenu> menuList) {
        List<SysMenuVO> menuVOList = menuList.stream()
            .filter(childMenu -> Objects.nonNull(childMenu.getParentId()))
            .filter(childMenu -> Objects.equals(parentMenu.getId(), childMenu.getParentId()))
            .map(childMenu -> {
                SysMenuVO entityToVO = SysMenuMapper.INSTANCE.entityToVO(childMenu);
                resolveChildMenu(entityToVO, menuList);
                return entityToVO;
            }).toList();
        parentMenu.setChildren(menuVOList);
    }
}

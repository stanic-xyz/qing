package cn.chenyunlong.qing.domain.auth.menu.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                                        .create(() -> SysMenuMapper.INSTANCE.dtoToEntity(creator))
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

    /**
     * valid
     */
    @Override
    public void validSysMenu(Long id) {
        EntityOperations.doUpdate(sysMenuRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
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
        return sysMenu.map(SysMenuMapper.INSTANCE::entityToVO).orElseThrow(() -> new BusinessException(CodeEnum.NotFindError));
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
        return menuList.stream().filter(sysMenu -> Objects.isNull(sysMenu.getParentId()))
                   .map(sysMenu -> {
                       SysMenuVO menuVO = SysMenuMapper.INSTANCE.entityToVO(sysMenu);
                       menuVO.setChildren(getChildMenu(sysMenu, menuList));
                       return menuVO;
                   })
                   .toList();
    }

    /**
     * 遍历查询子目录
     */
    private List<SysMenuVO> getChildMenu(SysMenu sysMenu, List<SysMenu> menuList) {
        Long parentId = sysMenu.getParentId();
        if (Objects.isNull(parentId)) {
            return Collections.emptyList();
        }
        return menuList.stream()
                   .filter(childMenu -> parentId.equals(sysMenu.getId()))
                   .map(childMenu -> {
                       SysMenuVO entityToVO = SysMenuMapper.INSTANCE.entityToVO(sysMenu);
                       entityToVO.setChildren(getChildMenu(childMenu, menuList));
                       return entityToVO;
                   }).toList();
    }
}

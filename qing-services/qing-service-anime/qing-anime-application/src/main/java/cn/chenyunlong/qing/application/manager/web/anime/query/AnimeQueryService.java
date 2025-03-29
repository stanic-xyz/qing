package cn.chenyunlong.qing.application.manager.web.anime.query;

import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeDetailVO;
import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
import cn.chenyunlong.qing.domain.auth.menu.mapper.SysMenuMapper;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth.MenuEntity;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnimeQueryService {

    private final AnimeCategoryJpaRepository animeCategoryRepository;
    private final AttachmentJpaRepository attachmentRepository;
    private final DistrictJpaRepository districtRepository;
    private final AnimeJpaRepository animeJpaRepository;
    private final SysMenuJpaRepository sysMenuJpaRepository;


    public List<SysMenuVO> treeMenu() {
        final List<MenuEntity> menuList = sysMenuJpaRepository.listAll();
        //        return menuList.stream()
        //            .filter(sysMenu -> Objects.isNull(sysMenu.getParentId()))
        //            .map(sysMenu -> {
        ////                SysMenuVO menuVO = SysMenuMapper.INSTANCE.entityToVO(sysMenu);
        //                //                resolveChildMenu(menuVO, menuList);
        //                resolveChildMenu(menuVO, CollUtil.toList());
        //                return menuVO;
        //            })
        //            .toList();
        return null;
    }

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

    public AnimeDetailVO findDetailById(Long id) {
        return null;
    }
}

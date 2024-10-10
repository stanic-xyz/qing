package cn.chenyunlong.qing.domain.auth.menu.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.menu.dto.creator.SysMenuCreator;
import cn.chenyunlong.qing.domain.auth.menu.dto.query.SysMenuQuery;
import cn.chenyunlong.qing.domain.auth.menu.dto.updater.SysMenuUpdater;
import cn.chenyunlong.qing.domain.auth.menu.dto.vo.SysMenuVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISysMenuService {

    /**
     * create
     */
    Long createSysMenu(SysMenuCreator creator);

    /**
     * update
     */
    void updateSysMenu(SysMenuUpdater updater);

    void validSysMenu(Long id);

    void invalidSysMenu(Long id);

    /**
     * findById
     */
    SysMenuVO findById(Long id);

    /**
     * findByPage
     */
    Page<SysMenuVO> findByPage(PageRequestWrapper<SysMenuQuery> query);

    /**
     * tree
     */
    List<SysMenuVO> tree();


}

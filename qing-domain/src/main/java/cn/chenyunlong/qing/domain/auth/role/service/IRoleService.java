package cn.chenyunlong.qing.domain.auth.role.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.role.dto.creator.RoleCreator;
import cn.chenyunlong.qing.domain.auth.role.dto.query.RoleQuery;
import cn.chenyunlong.qing.domain.auth.role.dto.updater.RoleUpdater;
import cn.chenyunlong.qing.domain.auth.role.dto.vo.RoleVO;
import org.springframework.data.domain.Page;

public interface IRoleService {

    /**
     * create
     */
    Long createRole(RoleCreator creator);

    /**
     * update
     */
    void updateRole(RoleUpdater updater);

    /**
     * valid
     */
    void validRole(Long id);

    /**
     * invalid
     */
    void invalidRole(Long id);

    /**
     * findById
     */
    RoleVO findById(Long id);

    /**
     * findByPage
     */
    Page<RoleVO> findByPage(PageRequestWrapper<RoleQuery> query);
}

package cn.chenyunlong.qing.domain.auth.admin.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.query.AdminAccountQuery;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.domain.auth.admin.dto.vo.AdminAccountVO;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IAdminAccountService {

    /**
     * create
     */
    Long createAdminAccount(AdminAccountCreator creator);

    /**
     * update
     */
    void updateAdminAccount(AdminAccountUpdater updater);

    /**
     * valid
     */
    void validAdminAccount(Long id);

    /**
     * invalid
     */
    void invalidAdminAccount(Long id);

    /**
     * findById
     */
    AdminAccountVO findById(Long id);

    /**
     * findByPage
     */
    Page<AdminAccountVO> findByPage(PageRequestWrapper<AdminAccountQuery> query);

    void assignRolesToUser(Long accountId, List<Long> roleIds);

    void assignPlatformsToUser(List<Long> platformIds, Long accountId);
}

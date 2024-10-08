package cn.chenyunlong.qing.domain.auth.admin.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.query.AdminAccountQuery;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.domain.auth.admin.dto.vo.AdminAccountVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAdminAccountService {

    /**
     * create
     */
    Long createAdminAccount(AdminAccountCreator creator);

    /**
     * update
     */
    void updateAdminAccount(AdminAccountUpdater updater);

    void validAdminAccount(Long id);

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

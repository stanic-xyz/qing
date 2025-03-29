package cn.chenyunlong.qing.domain.auth.admin.service;

import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;

import java.util.List;

public interface IAdminAccountService {

    Long createAdminAccount(AdminAccountCreator creator);

    void updateAdminAccount(AdminAccountUpdater updater);

    void validAdminAccount(Long id);

    void invalidAdminAccount(Long id);

    void assignRolesToUser(Long accountId, List<Long> roleIds);

    void assignPlatformsToUser(List<Long> platformIds, Long accountId);
}

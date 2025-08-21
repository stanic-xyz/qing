package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.admin.AdminAccount;
import cn.chenyunlong.qing.auth.domain.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.auth.domain.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.auth.domain.admin.repository.AdminAccountRepository;
import cn.chenyunlong.qing.auth.domain.admin.repository.AdminAccountRoleRelRepository;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.hutool.core.lang.Assert;
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
public class AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;
    private final RoleRepository roleRepository;
    private final AdminAccountRoleRelRepository adminAccountRoleRelRepository;

    public Long createAdminAccount(AdminAccountCreator creator) {
        Optional<AdminAccount> adminAccount = EntityOperations.doCreate(adminAccountRepository)
            .create(() -> AdminAccount.create(creator))
            .update(AdminAccount::init)
            .execute();
        return adminAccount.isPresent() ? adminAccount.get().getId().getId() : 0;
    }

    public void updateAdminAccount(AdminAccountUpdater updater) {
        EntityOperations.doUpdate(adminAccountRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateAdminAccount)
            .execute();
    }

    public void validAdminAccount(Long id) {
        EntityOperations.doUpdate(adminAccountRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    public void invalidAdminAccount(Long id) {
        EntityOperations.doUpdate(adminAccountRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }


    public void assignRolesToUser(Long accountId, List<Long> roleIds) {

    }


    public void assignPlatformsToUser(List<Long> platformIds, Long accountId) {
        Assert.notEmpty(platformIds, "平台id不能为空");
        // 判断用户是否存在
        Optional<AdminAccount> adminAccount = adminAccountRepository.findById(new AggregateId(accountId));
        Assert.isTrue(adminAccount.isPresent(), "账户不存在");

        //        // 查询当前用户所有的工作角色信息
        //        List<AdminAccountPlatformRel> adminAccountRoleRelList =
        //            adminAccountPlatformRelRepository.listPlatformsByAccountId(accountId);
        //        List<Long> oldPlatformRels =
        //            adminAccountRoleRelList.stream().map(AdminAccountPlatformRel::getId)
        //                .toList();
        //        if (oldPlatformRels.isEmpty()) {
        //            // 移除当前不存在的角色列表
        //            adminAccountPlatformRelRepository.deleteAllByIds(oldPlatformRels);
        //        }
        //        List<Platform> platformList = platformRepository.findByIds(platformIds);
        //        // 判断平台是否全部存在
        //        Assert.isTrue(CollUtil.size(platformList) == CollUtil.size(platformIds), "平台不存在");
        //        List<AdminAccountPlatformRel> roleRelList =
        //            platformList.stream().map(role -> AdminAccountPlatformRel.builder().adminAccountId(accountId).platformId(role.getId())
        //                .build()).toList();
        //        if (CollUtil.isNotEmpty(roleRelList)) {
        //            adminAccountPlatformRelRepository.saveAll(roleRelList);
        //        }
    }
}

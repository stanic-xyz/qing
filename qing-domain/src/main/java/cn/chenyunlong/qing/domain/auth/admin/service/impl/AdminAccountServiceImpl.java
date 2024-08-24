package cn.chenyunlong.qing.domain.auth.admin.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountPlatformRel;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.query.AdminAccountQuery;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.domain.auth.admin.dto.vo.AdminAccountVO;
import cn.chenyunlong.qing.domain.auth.admin.mapper.AdminAccountMapper;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountPlatformRelRepository;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRepository;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRoleRelRepository;
import cn.chenyunlong.qing.domain.auth.admin.service.IAdminAccountService;
import cn.chenyunlong.qing.domain.auth.platform.repository.PlatformRepository;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import java.util.List;
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
public class AdminAccountServiceImpl implements IAdminAccountService {

    private final AdminAccountRepository adminAccountRepository;
    private final RoleRepository roleRepository;
    private final AdminAccountRoleRelRepository adminAccountRoleRelRepository;
    private final PlatformRepository platformRepository;
    private final AdminAccountPlatformRelRepository adminAccountPlatformRelRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAdminAccount(AdminAccountCreator creator) {
        Optional<AdminAccount> adminAccount = EntityOperations.doCreate(adminAccountRepository)
                                                  .create(() -> AdminAccountMapper.INSTANCE.dtoToEntity(creator))
                                                  .update(AdminAccount::init)
                                                  .execute();
        return adminAccount.isPresent() ? adminAccount.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateAdminAccount(AdminAccountUpdater updater) {
        EntityOperations.doUpdate(adminAccountRepository)
            .loadById(updater.getId())
            .update(updater::updateAdminAccount)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validAdminAccount(Long id) {
        EntityOperations.doUpdate(adminAccountRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidAdminAccount(Long id) {
        EntityOperations.doUpdate(adminAccountRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public AdminAccountVO findById(Long id) {
        Optional<AdminAccount> adminAccount = adminAccountRepository.findById(id);
        return new AdminAccountVO(
            adminAccount.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<AdminAccountVO> findByPage(PageRequestWrapper<AdminAccountQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return adminAccountRepository.findAll(pageRequest).map(AdminAccountVO::new);
    }

    @Override
    public void assignRolesToUser(Long accountId, List<Long> roleIds) {
        Assert.notEmpty(roleIds, "角色不能为空");
        // 判断用户是否存在
        Optional<AdminAccount> adminAccount = adminAccountRepository.findById(accountId);
        Assert.isTrue(adminAccount.isPresent(), "账户不存在");

        // 查询当前用户所有的工作角色信息
        List<AdminAccountRoleRel> adminAccountRoleRelList =
            adminAccountRoleRelRepository.listRoleByAccountId(accountId);

        List<Long> oldRoleIds = adminAccountRoleRelList.stream().map(AdminAccountRoleRel::getId)
                                    .toList();
        if (oldRoleIds.isEmpty()) {
            // 移除当前不存在的角色列表
            adminAccountRoleRelRepository.deleteAllByIds(oldRoleIds);
        }
        List<AdminAccountRoleRel> roleRelList =
            roleRepository.findByIds(roleIds).stream().map(role ->
                                                               AdminAccountRoleRel.builder().adminAccountId(accountId).roleId(role.getId())
                                                                   .build()).toList();
        if (CollUtil.isNotEmpty(roleRelList)) {
            adminAccountRoleRelRepository.saveAll(roleRelList);
        }
    }

    @Override
    public void assignPlatformsToUser(List<Long> platformIds, Long accountId) {
        Assert.notEmpty(platformIds, "平台id不能为空");
        // 判断用户是否存在
        Optional<AdminAccount> adminAccount = adminAccountRepository.findById(accountId);
        Assert.isTrue(adminAccount.isPresent(), "账户不存在");

        // 查询当前用户所有的工作角色信息
        List<AdminAccountPlatformRel> adminAccountRoleRelList =
            adminAccountPlatformRelRepository.listPlatformsByAccountId(accountId);
        List<Long> oldPlatformRels =
            adminAccountRoleRelList.stream().map(AdminAccountPlatformRel::getId)
                .toList();
        if (oldPlatformRels.isEmpty()) {
            // 移除当前不存在的角色列表
            adminAccountPlatformRelRepository.deleteAllByIds(oldPlatformRels);
        }
        List<AdminAccountPlatformRel> roleRelList =
            platformRepository.findByIds(platformIds).stream().map(role ->
                                                                       AdminAccountPlatformRel.builder().adminAccountId(accountId).platformId(role.getId())
                                                                           .build()).toList();
        if (CollUtil.isNotEmpty(roleRelList)) {
            adminAccountPlatformRelRepository.saveAll(roleRelList);
        }
    }
}

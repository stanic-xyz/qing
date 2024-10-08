package cn.chenyunlong.qing.domain.auth.role.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.dto.creator.RoleCreator;
import cn.chenyunlong.qing.domain.auth.role.dto.query.RoleQuery;
import cn.chenyunlong.qing.domain.auth.role.dto.updater.RoleUpdater;
import cn.chenyunlong.qing.domain.auth.role.dto.vo.RoleVO;
import cn.chenyunlong.qing.domain.auth.role.mapper.RoleMapper;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleRepository;
import cn.chenyunlong.qing.domain.auth.role.service.IRoleService;
import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public Long createRole(RoleCreator creator) {

        Role role = roleRepository.findRoleByRole(creator.getRole());
        Assert.isNull(role, "角色已存在");

        Role byRoleName = roleRepository.findRoleByRoleName(creator.getName());
        Assert.isNull(byRoleName, "角色名称不能重复");

        Optional<Role> optionalRole = EntityOperations.doCreate(roleRepository)
            .create(() -> RoleMapper.INSTANCE.dtoToEntity(creator))
            .update(Role::init)
            .execute();
        return optionalRole.isPresent() ? optionalRole.get().getId() : 0;
    }

    @Override
    public void updateRole(RoleUpdater updater) {
        EntityOperations.doUpdate(roleRepository)
            .loadById(updater.getId())
            .update(updater::updateRole)
            .execute();
    }

    @Override
    public void validRole(Long id) {
        EntityOperations.doUpdate(roleRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    @Override
    public void invalidRole(Long id) {
        EntityOperations.doUpdate(roleRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public RoleVO findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return new RoleVO(role.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<RoleVO> findByPage(PageRequestWrapper<RoleQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return roleRepository.findAll(pageRequest).map(RoleVO::new);
    }

    /**
     * 根据用户查询角色
     */
    @Override
    public List<Role> listRoleByUser(Long userId) {
        return roleRepository.findRolesByUserId(userId);
    }
}

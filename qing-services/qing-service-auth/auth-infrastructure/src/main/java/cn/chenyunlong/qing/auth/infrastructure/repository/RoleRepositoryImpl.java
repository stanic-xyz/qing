package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.infrastructure.converter.RoleMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;


    @Override
    public List<Role> findByIds(List<RoleId> roleIds) {
        // 批量根据ID查询角色并映射为领域对象
        if (CollUtil.isEmpty(roleIds)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<Long> ids = roleIds.stream().map(RoleId::id).toList();
        return roleJpaRepository.findAllById(ids)
                .stream()
                .map(roleMapper::entity2Domain)
                .toList();
    }

    @Override
    public Role save(Role domain) {
        RoleEntity roleEntity = roleMapper.domain2Entity(domain);
        roleJpaRepository.save(roleEntity);
        return domain;
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return roleJpaRepository.findById(id.id()).map(roleMapper::entity2Domain);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleJpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }
}

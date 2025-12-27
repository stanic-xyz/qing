package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.infrastructure.converter.PermissionMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限仓储实现
 */
@Service
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionMapper permissionMapper;


    /**
     * 保存权限领域对象
     *
     * @param domain 权限领域对象
     * @return 保存后的领域对象
     */
    @Override
    public Permission save(Permission domain) {
        PermissionEntity entity = permissionMapper.toEntity(domain);
        permissionJpaRepository.save(entity);
        return domain;
    }

    /**
     * 根据ID查询权限
     *
     * @param id 权限ID
     * @return 权限领域对象Optional
     */
    @Override
    public Optional<Permission> findById(PermissionId id) {
        Optional<PermissionEntity> byId = permissionJpaRepository.findById(id.id());
        return byId.map(permissionMapper::entity2Domain);
    }

    /**
     * 判断权限编码是否存在
     *
     * @param code 权限编码
     * @return 是否存在
     */
    @Override
    public boolean existsByCode(String code) {
        return permissionJpaRepository.existsByCode(code);
    }

    /**
     * 判断权限名称是否存在
     *
     * @param name 权限名称
     * @return 是否存在
     */
    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    /**
     * 根据多个ID批量查询权限
     *
     * @param permissionIds 权限ID列表
     * @return 权限列表
     */
    @Override
    public List<Permission> findByIds(List<PermissionId> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return List.of();
        }
        return permissionJpaRepository.findAllById(permissionIds.stream().map(PermissionId::id).toList())
                .stream()
                .map(permissionMapper::entity2Domain)
                .collect(Collectors.toList());
    }
}

package cn.chenyunlong.qing.infrastructure.auth.repository;

import cn.chenyunlong.qing.auth.domain.role.Role;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.auth.repository.jpa.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;


    @Override
    public List<Role> findByIds(List<Long> roleIds) {
        return List.of();
    }

    @Override
    public Role save(Role entity) {
        return null;
    }

    @Override
    public Optional<Role> findById(AggregateId id) {
        return Optional.empty();
    }
}

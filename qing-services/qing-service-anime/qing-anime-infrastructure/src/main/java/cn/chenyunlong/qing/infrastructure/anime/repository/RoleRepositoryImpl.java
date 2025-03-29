package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.repository.RoleRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.RoleJpaRepository;
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

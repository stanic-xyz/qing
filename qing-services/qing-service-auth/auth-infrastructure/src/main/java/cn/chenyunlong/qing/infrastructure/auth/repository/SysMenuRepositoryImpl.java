package cn.chenyunlong.qing.infrastructure.auth.repository;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SysMenuRepositoryImpl implements SysMenuRepository {

    @Override
    public SysMenu save(SysMenu entity) {
        return null;
    }

    @Override
    public Optional<SysMenu> findById(AggregateId id) {
        return Optional.empty();
    }
}

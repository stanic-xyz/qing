package cn.chenyunlong.qing.auth.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.SysMenuId;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemorySysMenuRepository implements SysMenuRepository {
    @Override
    public SysMenu save(SysMenu entity) {
        return null;
    }

    @Override
    public Optional<SysMenu> findById(SysMenuId sysMenuId) {
        return Optional.empty();
    }
}

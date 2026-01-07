package cn.chenyunlong.qing.infrastructure.jpa.memory;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.SysMenuId;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySysMenuRepository implements SysMenuRepository {

    private final Map<SysMenuId, SysMenu> store = new ConcurrentHashMap<>();

    @Override
    public boolean existsByName(String menuName) {
        return store.values().stream().anyMatch(menu -> StrUtil.equals(menu.getMenuName(), menuName));
    }

    @Override
    public SysMenu save(SysMenu entity) {
        return store.put(entity.getId(), entity);
    }

    @Override
    public Optional<SysMenu> findById(SysMenuId sysMenuId) {
        return store.values().stream().filter(menu -> Objects.equals(sysMenuId.id(), menu.getId().id())).findFirst();
    }
}

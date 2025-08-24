package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.SysMenuId;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SysMenuRepositoryImpl implements SysMenuRepository {

    @Override
    public SysMenu save(SysMenu entity) {
        return null;
    }

    @Override
    public Optional<SysMenu> findById(SysMenuId id) {
        return Optional.empty();
    }

}

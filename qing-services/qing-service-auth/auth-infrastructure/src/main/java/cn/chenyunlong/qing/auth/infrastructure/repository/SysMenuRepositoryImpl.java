package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.menu.SysMenu;
import cn.chenyunlong.qing.auth.domain.menu.SysMenuId;
import cn.chenyunlong.qing.auth.domain.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.auth.infrastructure.converter.SysMenuMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.MenuEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.SysMenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SysMenuRepositoryImpl implements SysMenuRepository {

    private final SysMenuJpaRepository sysMenuJpaRepository;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public SysMenu save(SysMenu domain) {
        MenuEntity entity = sysMenuMapper.domain2Entity(domain);
        sysMenuJpaRepository.save(entity);
        return sysMenuMapper.entity2Domain(entity);
    }

    @Override
    public Optional<SysMenu> findById(SysMenuId id) {
        return sysMenuJpaRepository.findById(id.id()).map(sysMenuMapper::entity2Domain);
    }
}

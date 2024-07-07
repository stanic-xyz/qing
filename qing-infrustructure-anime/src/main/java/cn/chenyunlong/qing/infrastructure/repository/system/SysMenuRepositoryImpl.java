package cn.chenyunlong.qing.infrastructure.repository.system;

import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import cn.chenyunlong.qing.domain.auth.menu.repository.SysMenuRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.system.SysMenuJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SysMenuRepositoryImpl extends JpaServiceImpl<SysMenuJpaRepository, SysMenu, Long> implements SysMenuRepository {

}

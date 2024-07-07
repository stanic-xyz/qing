package cn.chenyunlong.qing.infrastructure.repository.jpa.system;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.menu.SysMenu;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMenuJpaRepository extends BaseJpaRepository<SysMenu, Long> {

}

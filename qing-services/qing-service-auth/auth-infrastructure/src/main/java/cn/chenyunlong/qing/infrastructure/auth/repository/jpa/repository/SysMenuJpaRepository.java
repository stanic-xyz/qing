package cn.chenyunlong.qing.infrastructure.auth.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.infrastructure.auth.repository.jpa.entity.MenuEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuJpaRepository extends BaseJpaRepository<MenuEntity, Long> {

    @Query("select menu from MenuEntity menu")
    List<MenuEntity> listAll();
}

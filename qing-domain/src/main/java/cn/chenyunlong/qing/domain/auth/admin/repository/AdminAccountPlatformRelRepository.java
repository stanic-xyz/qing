package cn.chenyunlong.qing.domain.auth.admin.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountPlatformRel;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface AdminAccountPlatformRelRepository
    extends BaseRepository<AdminAccountPlatformRel, Long> {

    @Query("select a from AdminAccountPlatformRel a where a.adminAccountId = ?1")
    List<AdminAccountPlatformRel> listPlatformsByAccountId(Long accountId);
}

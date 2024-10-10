package cn.chenyunlong.qing.domain.auth.admin.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountPlatformRel;

import java.util.List;

public interface AdminAccountPlatformRelRepository
    extends BaseRepository<AdminAccountPlatformRel, Long> {

    List<AdminAccountPlatformRel> listPlatformsByAccountId(Long accountId);
}

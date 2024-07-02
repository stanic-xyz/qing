package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.PlatformJpaRepository;
import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.auth.platform.repository.PlatformRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentRepositoryImpl extends JpaServiceImpl<PlatformJpaRepository, Platform, Long> implements PlatformRepository {

}

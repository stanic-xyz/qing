package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.auth.platform.repository.PlatformRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.PlatformJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PlatformRepositoryImpl extends JpaServiceImpl<PlatformJpaRepository, Platform, Long> implements PlatformRepository {

}

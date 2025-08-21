package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.platform.Platform;
import cn.chenyunlong.qing.auth.domain.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.auth.domain.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.auth.domain.platform.repository.PlatformRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository platformRepository;

    /**
     * createImpl
     */
    public Long createPlatform(PlatformCreator creator) {
        Optional<Platform> platform = EntityOperations.doCreate(platformRepository)
            .create(() -> Platform.create(creator))
            .update(Platform::init)
            .execute();
        return platform.isPresent() ? platform.get().getId().getId() : 0;
    }

    public void updatePlatform(PlatformUpdater updater) {
    }

    public void validPlatform(Long id) {
    }

    public void invalidPlatform(Long id) {
    }

}

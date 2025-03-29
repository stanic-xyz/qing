package cn.chenyunlong.qing.domain.auth.platform.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;
import cn.chenyunlong.qing.domain.auth.platform.mapper.PlatformMapper;
import cn.chenyunlong.qing.domain.auth.platform.repository.PlatformRepository;
import cn.chenyunlong.qing.domain.auth.platform.service.IPlatformService;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformServiceImpl implements IPlatformService {

    private final PlatformRepository platformRepository;

    /**
     * createImpl
     */
    @Override
    public Long createPlatform(PlatformCreator creator) {
        Optional<Platform> platform = EntityOperations.doCreate(platformRepository)
            .create(() -> PlatformMapper.INSTANCE.dtoToEntity(creator))
            .update(Platform::init)
            .execute();
        return platform.isPresent() ? platform.get().getAggregateId().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updatePlatform(PlatformUpdater updater) {
    }

    @Override
    public void validPlatform(Long id) {
    }

    @Override
    public void invalidPlatform(Long id) {
    }

}

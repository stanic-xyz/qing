package cn.chenyunlong.qing.domain.auth.platform.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.query.PlatformQuery;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;
import cn.chenyunlong.qing.domain.auth.platform.mapper.PlatformMapper;
import cn.chenyunlong.qing.domain.auth.platform.repository.PlatformRepository;
import cn.chenyunlong.qing.domain.auth.platform.service.IPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        return platform.isPresent() ? platform.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updatePlatform(PlatformUpdater updater) {
        EntityOperations.doUpdate(platformRepository)
                .loadById(updater.getId())
                .update(updater::updatePlatform)
                .execute();
    }

    /**
     * valid
     */
    @Override
    public void validPlatform(Long id) {
        EntityOperations.doUpdate(platformRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidPlatform(Long id) {
        EntityOperations.doUpdate(platformRepository)
                .loadById(id)
                .update(BaseJpaAggregate::invalid)
                .execute();
    }

    /**
     * findById
     */
    @Override
    public PlatformVO findById(Long id) {
        Optional<Platform> platform = platformRepository.findById(id);
        return new PlatformVO(platform.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<PlatformVO> findByPage(PageRequestWrapper<PlatformQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return platformRepository.findAll(pageRequest).map(PlatformVO::new);
    }
}

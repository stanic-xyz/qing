// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.system.job.log.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.system.job.log.SysJobLog;
import cn.chenyunlong.qing.domain.system.job.log.creator.SysJobLogCreator;
import cn.chenyunlong.qing.domain.system.job.log.mapper.SysJobLogMapper;
import cn.chenyunlong.qing.domain.system.job.log.query.SysJobLogQuery;
import cn.chenyunlong.qing.domain.system.job.log.repository.SysJobLogRepository;
import cn.chenyunlong.qing.domain.system.job.log.service.ISysJobLogService;
import cn.chenyunlong.qing.domain.system.job.log.updater.SysJobLogUpdater;
import cn.chenyunlong.qing.domain.system.job.log.vo.SysJobLogVO;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class SysJobLogServiceImpl implements ISysJobLogService {
    private final SysJobLogRepository sysJobLogRepository;

    /**
     * createImpl
     */
    @Override
    public Long createSysJobLog(SysJobLogCreator creator) {
        Optional<SysJobLog> sysJobLog = EntityOperations.doCreate(sysJobLogRepository)
                .create(() -> SysJobLogMapper.INSTANCE.dtoToEntity(creator))
                .update(e -> e.init())
                .execute();
        return sysJobLog.isPresent() ? sysJobLog.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateSysJobLog(SysJobLogUpdater updater) {
        EntityOperations.doUpdate(sysJobLogRepository)
                .loadById(updater.getId())
                .update(e -> updater.updateSysJobLog(e))
                .execute();
    }

    /**
     * valid
     */
    @Override
    public void validSysJobLog(Long id) {
        EntityOperations.doUpdate(sysJobLogRepository)
                .loadById(id)
                .update(e -> e.valid())
                .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidSysJobLog(Long id) {
        EntityOperations.doUpdate(sysJobLogRepository)
                .loadById(id)
                .update(e -> e.invalid())
                .execute();
    }

    /**
     * findById
     */
    @Override
    public SysJobLogVO findById(Long id) {
        Optional<SysJobLog> sysJobLog = sysJobLogRepository.findById(id);
        return new SysJobLogVO(sysJobLog.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<SysJobLogVO> findByPage(PageRequestWrapper<SysJobLogQuery> query) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Page<SysJobLog> page = sysJobLogRepository.findAll(booleanBuilder,
                PageRequest.of(query.getPage() - 1, query.getPageSize(), Sort.by(
                        Sort.Direction.DESC, "createdAt")));
        return new PageImpl<>(page.getContent()
                .stream().map(SysJobLogVO::new)
                .collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }
}
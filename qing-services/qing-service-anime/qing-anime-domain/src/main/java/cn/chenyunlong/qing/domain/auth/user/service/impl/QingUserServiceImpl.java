package cn.chenyunlong.qing.domain.auth.user.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.converter.SysUserMapper;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.QingUserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.QingUserVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.QingUserMapper;
import cn.chenyunlong.qing.domain.auth.user.repository.QingUserRepository;
import cn.chenyunlong.qing.domain.auth.user.service.IQingUserService;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class QingUserServiceImpl implements IQingUserService {

    private final QingUserRepository qingUserRepository;

    /**
     * createImpl
     */
    @Override
    public Long createQingUser(QingUserCreator creator) {
        Optional<QingUser> qingUser = EntityOperations.doCreate(qingUserRepository)
            .create(() -> QingUserMapper.INSTANCE.dtoToEntity(creator))
            .update(QingUser::init)
            .execute();
        return qingUser.isPresent() ? qingUser.get().getAggregateId().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateQingUser(QingUserUpdater updater) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateQingUser)
            .execute();
    }

    @Override
    public void validQingUser(Long id) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidQingUser(Long id) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public QingUserVO findById(Long id) {
        Optional<QingUser> qingUser = qingUserRepository.findById(new AggregateId(id));
        return qingUser.map(SysUserMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }
}

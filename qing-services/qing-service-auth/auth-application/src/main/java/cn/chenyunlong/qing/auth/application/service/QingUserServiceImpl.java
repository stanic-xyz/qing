package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.updater.QingUserUpdater;
import cn.chenyunlong.qing.auth.domain.user.dto.vo.QingUserVO;
import cn.chenyunlong.qing.auth.domain.user.repository.QingUserRepository;
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
public class QingUserServiceImpl {

    private final QingUserRepository qingUserRepository;

    /**
     * createImpl
     */

    public Long createQingUser(QingUserCreator creator) {
        Optional<QingUser> qingUser = EntityOperations.doCreate(qingUserRepository)
            .create(() -> this.dtoToEntity(creator))
            .update(QingUser::init)
            .execute();
        return qingUser.isPresent() ? qingUser.get().getId().getId() : 0;
    }

    private QingUser dtoToEntity(QingUserCreator creator) {
        return null;
    }

    /**
     * update
     */

    public void updateQingUser(QingUserUpdater updater) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateQingUser)
            .execute();
    }


    public void validQingUser(Long id) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }


    public void invalidQingUser(Long id) {
        EntityOperations.doUpdate(qingUserRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */

    public QingUserVO findById(Long id) {
        Optional<QingUser> qingUser = qingUserRepository.findById(new AggregateId(id));
        return qingUser.map(this::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private QingUserVO entityToVo(QingUser qingUser) {
        return null;
    }
}

package cn.chenyunlong.qing.domain.auth.user.service.impl;

import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.domain.auth.user.mapper.UserTokenMapper;
import cn.chenyunlong.qing.domain.auth.user.repository.UserTokenRepository;
import cn.chenyunlong.qing.domain.auth.user.service.IUserTokenService;
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
public class UserTokenServiceImpl implements IUserTokenService {

    private final UserTokenRepository userTokenRepository;

    /**
     * createImpl
     */
    @Override
    public Long createUserToken(UserTokenCreator creator) {
        Optional<UserToken> userToken = EntityOperations.doCreate(userTokenRepository)
            .create(() -> UserTokenMapper.INSTANCE.dtoToEntity(creator))
            .update(UserToken::init)
            .execute();
        return userToken.isPresent() ? userToken.orElseThrow().getUid() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateUserToken(UserTokenUpdater updater) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateUserToken)
            .execute();
    }

    @Override
    public void validUserToken(Long id) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidUserToken(Long id) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }
}

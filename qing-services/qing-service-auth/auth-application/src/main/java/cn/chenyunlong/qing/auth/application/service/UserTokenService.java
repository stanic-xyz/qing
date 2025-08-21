package cn.chenyunlong.qing.auth.app.application.service;

import cn.chenyunlong.qing.auth.domain.user.UserToken;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.auth.domain.user.repository.UserTokenRepository;
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
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    /**
     * createImpl
     */
    public Long createUserToken(UserTokenCreator creator) {
        Optional<UserToken> userToken = EntityOperations.doCreate(userTokenRepository)
            .create(() -> UserToken.create(creator))
            .update(UserToken::init)
            .execute();
        return userToken.isPresent() ? userToken.orElseThrow().getUid() : 0;
    }

    /**
     * update
     */
    public void updateUserToken(UserTokenUpdater updater) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateUserToken)
            .execute();
    }

    public void validUserToken(Long id) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    public void invalidUserToken(Long id) {
        EntityOperations.doUpdate(userTokenRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }
}

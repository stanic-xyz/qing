package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;

public interface IUserTokenService {

    /**
     * create
     */
    Long createUserToken(UserTokenCreator creator);

    /**
     * update
     */
    void updateUserToken(UserTokenUpdater updater);

    void validUserToken(Long id);

    void invalidUserToken(Long id);
}

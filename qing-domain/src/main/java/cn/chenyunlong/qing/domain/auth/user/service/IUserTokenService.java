package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserTokenQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserTokenVO;
import org.springframework.data.domain.Page;

public interface IUserTokenService {
    /**
     * create
     */
    Long createUserToken(UserTokenCreator creator);

    /**
     * update
     */
    void updateUserToken(UserTokenUpdater updater);

    /**
     * valid
     */
    void validUserToken(Long id);

    /**
     * invalid
     */
    void invalidUserToken(Long id);

    /**
     * findById
     */
    UserTokenVO findById(Long id);

    /**
     * findByPage
     */
    Page<UserTokenVO> findByPage(PageRequestWrapper<UserTokenQuery> query);
}

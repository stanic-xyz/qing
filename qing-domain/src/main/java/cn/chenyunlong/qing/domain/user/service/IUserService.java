package cn.chenyunlong.qing.domain.user.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.user.dto.vo.UserVO;
import org.springframework.data.domain.Page;

public interface IUserService {
    /**
     * create
     */
    Long createUser(UserCreator creator);

    /**
     * update
     */
    void updateUser(UserUpdater updater);

    /**
     * valid
     */
    void validUser(Long id);

    /**
     * invalid
     */
    void invalidUser(Long id);

    /**
     * findById
     */
    UserVO findById(Long id);

    /**
     * findByPage
     */
    Page<UserVO> findByPage(PageRequestWrapper<UserQuery> query);
}

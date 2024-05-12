package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface IUserService {

    /**
     * create
     */
    Long register(UserCreator creator);

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

    Optional<QingUser> findByUsername(String username);

    Optional<QingUser> loadUserByUserId(String userId);
}

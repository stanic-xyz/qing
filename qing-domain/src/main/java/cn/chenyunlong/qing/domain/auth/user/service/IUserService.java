package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    /**
     * 根据用户名查询用户
     */
    Optional<QingUser> findByUsername(String username);

    /**
     * 根据用户名查询用户
     */
    Optional<QingUser> findByEmail(String email);

    /**
     * 根据用户id查询用户
     */
    Optional<QingUser> loadUserByUserId(String userId);

    /**
     * 根据用户昵称查询用户，查询用户昵称是否冲突
     */
    List<QingUser> findByUserNickNames(Set<String> nickNames);
}

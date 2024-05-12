package cn.chenyunlong.qing.domain.auth.connection.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.query.UserConnectionQuery;
import cn.chenyunlong.qing.domain.auth.connection.dto.updater.UserConnectionUpdater;
import cn.chenyunlong.qing.domain.auth.connection.dto.vo.UserConnectionVO;
import org.springframework.data.domain.Page;

public interface IUserConnectionService {

    /**
     * create
     */
    Long createUserConnection(UserConnectionCreator creator);

    /**
     * update
     */
    void updateUserConnection(UserConnectionUpdater updater);

    /**
     * valid
     */
    void validUserConnection(Long id);

    /**
     * invalid
     */
    void invalidUserConnection(Long id);

    /**
     * findById
     */
    UserConnectionVO findById(Long id);

    /**
     * findByPage
     */
    Page<UserConnectionVO> findByPage(PageRequestWrapper<UserConnectionQuery> query);
}

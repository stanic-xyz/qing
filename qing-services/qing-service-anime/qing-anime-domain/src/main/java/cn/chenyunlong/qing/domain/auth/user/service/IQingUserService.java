package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.qing.domain.auth.user.dto.creator.QingUserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.QingUserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.QingUserVO;

public interface IQingUserService {

    /**
     * create
     */
    Long createQingUser(QingUserCreator creator);

    /**
     * update
     */
    void updateQingUser(QingUserUpdater updater);

    void validQingUser(Long id);

    void invalidQingUser(Long id);

    /**
     * findById
     */
    QingUserVO findById(Long id);
}

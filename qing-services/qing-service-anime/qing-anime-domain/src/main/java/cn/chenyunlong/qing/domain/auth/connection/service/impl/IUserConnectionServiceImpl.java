package cn.chenyunlong.qing.domain.auth.connection.service.impl;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.query.UserConnectionQuery;
import cn.chenyunlong.qing.domain.auth.connection.dto.updater.UserConnectionUpdater;
import cn.chenyunlong.qing.domain.auth.connection.dto.vo.UserConnectionVO;
import cn.chenyunlong.qing.domain.auth.connection.service.IUserConnectionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class IUserConnectionServiceImpl implements IUserConnectionService {

    @Override
    public Long createUserConnection(UserConnectionCreator creator) {
        return null;
    }

    @Override
    public void updateUserConnection(UserConnectionUpdater updater) {

    }

    @Override
    public void validUserConnection(Long id) {

    }

    @Override
    public void invalidUserConnection(Long id) {

    }

    @Override
    public UserConnectionVO findById(Long id) {
        return null;
    }

    @Override
    public Page<UserConnectionVO> findByPage(PageRequestWrapper<UserConnectionQuery> query) {
        return null;
    }
}

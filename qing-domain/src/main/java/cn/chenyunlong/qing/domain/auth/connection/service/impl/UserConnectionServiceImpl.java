package cn.chenyunlong.qing.domain.auth.connection.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.query.UserConnectionQuery;
import cn.chenyunlong.qing.domain.auth.connection.dto.updater.UserConnectionUpdater;
import cn.chenyunlong.qing.domain.auth.connection.dto.vo.UserConnectionVO;
import cn.chenyunlong.qing.domain.auth.connection.mapper.UserConnectionMapper;
import cn.chenyunlong.qing.domain.auth.connection.repository.UserConnectionRepository;
import cn.chenyunlong.qing.domain.auth.connection.service.IUserConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserConnectionServiceImpl implements IUserConnectionService {
    private final UserConnectionRepository userConnectionRepository;

    /**
     * createImpl
     */
    @Override
    public Long createUserConnection(UserConnectionCreator creator) {
        Optional<UserConnection> userConnection = EntityOperations.doCreate(userConnectionRepository)
                .create(() -> UserConnectionMapper.INSTANCE.dtoToEntity(creator))
                .update(UserConnection::init)
                .execute();
        return userConnection.isPresent() ? userConnection.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateUserConnection(UserConnectionUpdater updater) {
        EntityOperations.doUpdate(userConnectionRepository)
                .loadById(updater.getId())
                .update(updater::updateUserConnection)
                .execute();
    }

    /**
     * valid
     */
    @Override
    public void validUserConnection(Long id) {
        EntityOperations.doUpdate(userConnectionRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidUserConnection(Long id) {
        EntityOperations.doUpdate(userConnectionRepository)
                .loadById(id)
                .update(BaseJpaAggregate::invalid)
                .execute();
    }

    /**
     * findById
     */
    @Override
    public UserConnectionVO findById(Long id) {
        Optional<UserConnection> userConnection = userConnectionRepository.findById(id);
        return new UserConnectionVO(userConnection.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<UserConnectionVO> findByPage(PageRequestWrapper<UserConnectionQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return userConnectionRepository.findAll(pageRequest).map(UserConnectionVO::new);
    }
}

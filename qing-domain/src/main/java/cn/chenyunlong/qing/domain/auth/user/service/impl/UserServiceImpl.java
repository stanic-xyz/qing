package cn.chenyunlong.qing.domain.auth.user.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.user.User;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.UserMapper;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
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
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    /**
     * createImpl
     */
    @Override
    public Long createUser(UserCreator creator) {
        Optional<User> user = EntityOperations.doCreate(userRepository)
                .create(() -> UserMapper.INSTANCE.dtoToEntity(creator))
                .update(User::init)
                .execute();
        return user.isPresent() ? user.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateUser(UserUpdater updater) {
        EntityOperations.doUpdate(userRepository)
                .loadById(updater.getId())
                .update(updater::updateUser)
                .execute();
    }

    /**
     * valid
     */
    @Override
    public void validUser(Long id) {
        EntityOperations.doUpdate(userRepository)
                .loadById(id)
                .update(BaseJpaAggregate::valid)
                .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidUser(Long id) {
        EntityOperations.doUpdate(userRepository)
                .loadById(id)
                .update(BaseJpaAggregate::invalid)
                .execute();
    }

    /**
     * findById
     */
    @Override
    public UserVO findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return new UserVO(user.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<UserVO> findByPage(PageRequestWrapper<UserQuery> query) {
        PageRequest pageRequest =
                PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return userRepository.findAll(pageRequest).map(UserVO::new);
    }
}

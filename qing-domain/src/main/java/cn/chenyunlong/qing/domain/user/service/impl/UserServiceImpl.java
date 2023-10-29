package cn.chenyunlong.qing.domain.user.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import cn.chenyunlong.qing.domain.user.User;
import cn.chenyunlong.qing.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.user.dto.vo.UserVO;
import cn.chenyunlong.qing.domain.user.mapper.UserMapper;
import cn.chenyunlong.qing.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.user.service.IUserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            .update(BaseEntity::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidUser(Long id) {
        EntityOperations.doUpdate(userRepository)
            .loadById(id)
            .update(BaseEntity::invalid)
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

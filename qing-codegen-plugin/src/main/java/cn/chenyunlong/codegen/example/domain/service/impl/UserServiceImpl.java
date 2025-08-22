package cn.chenyunlong.codegen.example.domain.service.impl;

import cn.chenyunlong.codegen.example.domain.User;
import cn.chenyunlong.codegen.example.domain.dto.creator.UserCreator;
import cn.chenyunlong.codegen.example.domain.dto.query.UserQuery;
import cn.chenyunlong.codegen.example.domain.dto.updater.UserUpdater;
import cn.chenyunlong.codegen.example.domain.dto.vo.UserVO;
import cn.chenyunlong.codegen.example.domain.mapper.UserMapper;
import cn.chenyunlong.codegen.example.domain.repository.UserRepository;
import cn.chenyunlong.codegen.example.domain.service.IUserService;
import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import java.lang.Long;
import java.lang.Override;
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
    public void validUser(AggregateId id) {
        EntityOperations.doUpdate(userRepository)
            .loadById(id)
            .update(User::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidUser(AggregateId id) {
        EntityOperations.doUpdate(userRepository)
            .loadById(id)
            .update(User::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public UserVO findById(AggregateId id) {
        Optional<User> user =  userRepository.findById(id);
        return new UserVO(user.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<UserVO> findByPage(PageRequestWrapper<UserQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return userRepository.findAll(pageRequest).map(UserVO::new);
    }
}

package cn.chenyunlong.qing.domain.auth.user.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.converter.SysUserMapper;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.UserMapper;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public Long register(UserCreator creator) {
        Optional<QingUser> user = EntityOperations.doCreate(userRepository)
                                      .create(() -> UserMapper.INSTANCE.dtoToEntity(creator))
                                      .update(QingUser::init)
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
        Optional<QingUser> user = userRepository.findById(id);
        return user.map(SysUserMapper.INSTANCE::entityToUserVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    /**
     * findByPage
     */
    @Override
    public Page<UserVO> findByPage(PageRequestWrapper<UserQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return userRepository.findAll(pageRequest).map(SysUserMapper.INSTANCE::entityToUserVo);
    }

    @Override
    public Optional<QingUser> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public Optional<QingUser> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public Optional<QingUser> loadUserByUserId(String userId) {
        QingUser qingUser = userRepository.findUserByUserId(userId);
        return Optional.ofNullable(qingUser);
    }

    @Override
    public List<QingUser> findByUserNickNames(Set<String> nickNames) {
        return userRepository.findByUserNames(nickNames);
    }
}

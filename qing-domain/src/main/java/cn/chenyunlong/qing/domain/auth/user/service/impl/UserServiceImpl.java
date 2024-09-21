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
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * createImpl
     */
    @Override
    public Optional<QingUser> register(UserCreator creator) {
        QingUser byUsername = userRepository.findByUsername(creator.getUsername());
        Assert.isNull(byUsername, "用户名已存在");
        QingUser byEmail = userRepository.findByEmail(creator.getEmail());
        Assert.isNull(byEmail, "邮箱已存在");
        return EntityOperations.doCreate(userRepository)
                   .create(() -> {
                       QingUser qingUser = UserMapper.INSTANCE.dtoToEntity(creator);
                       // 生成用户唯一Id
                       qingUser.setUid(IdUtil.getSnowflakeNextId());
                       qingUser.setPassword(passwordEncoder.encode(creator.getPassword()));
                       return qingUser;
                   })
                   .update(QingUser::init)
                   .errorHook(Throwable::printStackTrace)
                   .execute();
    }

    /**
     * update
     */
    @Override
    public void updateUser(UserUpdater updater) {
        EntityOperations.doUpdate(userRepository)
            .loadById(updater.getId())
            .update(qingUser -> updater.updateUser(qingUser, passwordEncoder))
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

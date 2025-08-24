package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.infrastructure.converter.UserMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.QingUserEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserConnectionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserJpaRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserConnectionJpaRepository userConnectionJpaRepository;

    @Override
    public QingUser findByUsername(String username) {
        QingUserEntity byUsername = userJpaRepository.findByUsername(username);
        if (byUsername == null) {
            return null;
        }
        return UserMapper.INSTANCE.entityToDomain(byUsername);
    }

    @Override
    public QingUser findUserByUserId(String userId) {
        QingUserEntity userEntity = userJpaRepository.findUserByUserId(userId);
        if (userEntity == null) {
            return null;
        }
        return UserMapper.INSTANCE.entityToDomain(userEntity);
    }

    @Override
    public List<QingUser> findByUserNames(Set<String> nickNames) {
        if (CollUtil.isEmpty(nickNames)) {
            return CollUtil.newArrayList();
        }
        return userJpaRepository.findByNickNames(nickNames).stream().map(UserMapper.INSTANCE::entityToDomain).toList();
    }

    @Override
    public QingUser findByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        QingUserEntity byEmail = userJpaRepository.findByEmail(email);
        if (byEmail == null) {
            return null;
        }
        return UserMapper.INSTANCE.entityToDomain(byEmail);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public QingUser save(QingUser entity) {
        return null;
    }

    @Override
    public Optional<QingUser> findById(QingUserId id) {
        return Optional.empty();
    }
}

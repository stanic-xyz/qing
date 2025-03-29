package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.auth.UserJpaRepository;
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

    @Override
    public QingUser findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public QingUser findUserByUserId(String userId) {
        return userJpaRepository.findUserByUserId(userId);
    }

    @Override
    public List<QingUser> findByUserNames(Set<String> nickNames) {
        if (CollUtil.isEmpty(nickNames)) {
            return CollUtil.newArrayList();
        }
        return userJpaRepository.findByNickNames(nickNames);
    }

    @Override
    public QingUser findByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public QingUser save(QingUser entity) {
        return null;
    }

    @Override
    public Optional<QingUser> findById(AggregateId id) {
        return Optional.empty();
    }
}

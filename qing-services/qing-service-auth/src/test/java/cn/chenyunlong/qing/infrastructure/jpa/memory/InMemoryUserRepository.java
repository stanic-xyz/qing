package cn.chenyunlong.qing.infrastructure.jpa.memory;

import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.PhoneNumber;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import cn.hutool.core.util.StrUtil;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByUsername(Username username) {
        return store.values().stream().filter(qingUser -> qingUser.getUsername().value().equals(username.value())).findFirst();
    }

    @Override
    public Optional<User> findUserByUserId(Long uid) {
        return store.values().stream()
                .filter(user -> user.getId().id().equals(uid))
                .findFirst();
    }

    @Override
    public List<User> findByUserNames(Set<Username> nickNames) {
        return store.values().stream()
                .filter(user -> nickNames.stream().map(Username::value).anyMatch(name -> name.equals(user.getUsername().value())))
                .toList();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return store.values().stream()
                .filter(user -> user.getEmail().value().equals(email.value()))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(Username username) {
        return store.values().stream().anyMatch(qingUser -> StrUtil.equals(qingUser.getUsername().value(), username.value()));
    }

    @Override
    public boolean existsByEmail(Email email) {
        return store.values().stream().anyMatch(qingUser -> Objects.equals(qingUser.getEmail().value(), email.value()));
    }

    @Override
    public boolean existsByPhone(PhoneNumber phone) {
        return store.values().stream().anyMatch(qingUser -> StrUtil.equals(qingUser.getPhone().value(), phone.value()));
    }

    @Override
    public User save(User entity) {
        store.put(entity.getId().id(), entity);
        return entity;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.ofNullable(store.get(id.id()));
    }

    @Override
    public boolean existsByNicknames(String nickname) {
        return store.values().stream().anyMatch(qingUser -> StrUtil.equals(qingUser.getNickname(), nickname));
    }
}

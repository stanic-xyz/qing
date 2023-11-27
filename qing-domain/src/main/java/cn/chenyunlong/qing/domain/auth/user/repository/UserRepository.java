package cn.chenyunlong.qing.domain.auth.user.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.user.User;

public interface UserRepository extends BaseRepository<User, Long> {
    void findByUsername(String username);
}

package cn.chenyunlong.qing.domain.auth.user.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.user.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends BaseRepository<User, Long> {

    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);
}

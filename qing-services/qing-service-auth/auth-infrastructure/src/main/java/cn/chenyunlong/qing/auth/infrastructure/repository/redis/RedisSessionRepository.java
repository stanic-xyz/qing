package cn.chenyunlong.qing.auth.infrastructure.repository.redis;

import cn.chenyunlong.qing.auth.domain.user.valueObject.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisSessionRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveSession(String sessionId, Session session) {
        redisTemplate.opsForValue().set("session:" + sessionId, session, 24, TimeUnit.HOURS);
    }

    public Session getSession(String sessionId) {
        return (Session) redisTemplate.opsForValue().get("session:" + sessionId);
    }

    public void deleteSession(String sessionId) {
        redisTemplate.delete("session:" + sessionId);
    }

    public boolean isSessionValid(String sessionId) {
        Session session = getSession(sessionId);
        return session != null && session.isActive() && !session.isExpired();
    }
}

package cn.chenyunlong.qing.service.llm.service.lock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// 本地锁实现（基于 ReentrantLock）
@Component
@ConditionalOnProperty(name = "match.lock.type", havingValue = "local", matchIfMissing = true)
public class LocalLockFactory implements LockFactory {

    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public Lock getLock(String key) {
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        return new Lock() {
            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return lock.tryLock(time, unit);
            }

            @Override
            public void unlock() {
                lock.unlock();
            }
        };
    }
}

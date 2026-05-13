package cn.chenyunlong.qing.service.llm.service.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@Component
//@ConditionalOnProperty(name = "match.lock.type", havingValue = "redis")
//public class RedisLockFactory implements LockFactory {
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Override
//    public Lock getLock(String key) {
//        RLock rLock = redissonClient.getLock(key);
//        return new Lock() {
//            @Override
//            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//                return rLock.tryLock(time, unit);
//            }
//
//            @Override
//            public void unlock() {
//                if (rLock.isHeldByCurrentThread()) {
//                    rLock.unlock();
//                }
//            }
//        };
//    }
//}

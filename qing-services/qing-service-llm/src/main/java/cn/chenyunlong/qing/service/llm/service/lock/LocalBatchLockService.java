package cn.chenyunlong.qing.service.llm.service.lock;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

// 单机实现（当前用）
@Service
public class LocalBatchLockService implements BatchLockService {

    private final ConcurrentHashMap<String, Boolean> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryLock(String batchKey) {
        return lockMap.putIfAbsent(batchKey, Boolean.TRUE) == null;
    }

    @Override
    public void unlock(String batchKey) {
        lockMap.remove(batchKey);
    }
}

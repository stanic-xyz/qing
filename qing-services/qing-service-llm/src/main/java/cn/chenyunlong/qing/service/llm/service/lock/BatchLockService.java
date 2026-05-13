package cn.chenyunlong.qing.service.llm.service.lock;

public interface BatchLockService {
    boolean tryLock(String batchKey);

    void unlock(String batchKey);
}

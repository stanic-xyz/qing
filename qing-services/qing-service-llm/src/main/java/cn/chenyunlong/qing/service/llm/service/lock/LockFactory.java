package cn.chenyunlong.qing.service.llm.service.lock;

/**
 * 锁工厂接口，用于获取分布式锁或本地锁
 */
public interface LockFactory {

    /**
     * 根据 key 获取锁实例
     *
     * @param key 锁的唯一标识（如批次ID）
     * @return Lock 对象
     */
    Lock getLock(String key);
}

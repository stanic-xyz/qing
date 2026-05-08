package cn.chenyunlong.qing.service.llm.service.lock;

import java.util.concurrent.TimeUnit;

/**
 * 锁接口，抽象了加锁/解锁行为
 */
public interface Lock {

    /**
     * 尝试获取锁，等待指定时间
     *
     * @param time 等待时长
     * @param unit 时间单位
     * @return true 获取成功，false 获取失败
     * @throws InterruptedException 线程中断异常
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * 释放锁
     */
    void unlock();
}

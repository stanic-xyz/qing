package cn.chenyunlong.qing.service.llm.thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

@Disabled
public class TestConcurrencyTools {

    @Test
    public void testLongAdder() throws Exception {

        LongAdder counter = new LongAdder();
        System.out.println("Counter: " + counter.sum());

        // 对比AtomicLong
        System.out.println("LongAdder vs AtomicLong");

        CountDownLatch latch = new CountDownLatch(100);
        // 计时开始
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    counter.increment();
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        // 计时结束
        long endTime = System.currentTimeMillis();
        System.out.println("Counter: " + counter.sum());
        System.out.println("LongAdder Time taken: " + (endTime - startTime) + " ms");

        Assertions.assertEquals(100000000, counter.sum());
    }

    @Test
    public void testAtomicLong() throws InterruptedException {
        AtomicLong atomicLong = new AtomicLong();
        CountDownLatch atomLatch = new CountDownLatch(100);
        System.out.println("AtomicLong: " + atomicLong.get());

        // 计时开始
        long atomStartTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    atomicLong.getAndIncrement();
                }
                atomLatch.countDown();
            }).start();
        }
        atomLatch.await();

        // 计时结束
        long atomEndTime = System.currentTimeMillis();
        System.out.println("Counter: " + atomicLong.get());
        System.out.println("AtomicLong Time taken: " + (atomEndTime - atomStartTime) + " ms");

        Assertions.assertEquals(100000000, atomicLong.get());

    }

    @Test
    public void testLongAccumulator() {
        // 求最大值的累加器
        LongAccumulator maxAccumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
        maxAccumulator.accumulate(100);
        maxAccumulator.accumulate(50);
        maxAccumulator.accumulate(200);

        System.out.println(maxAccumulator.get());  // 输出 200
    }

    @Test
    public void testSemaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2, true);
        System.out.println("Semaphore: " + semaphore.availablePermits());

        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            // 反映排队情况
            new Thread(() -> {
                {
                    try {
                        System.out.println(MessageFormat.format("{0} Acquire permit： 当前时间：「{1}」", Thread.currentThread().getName(), System.currentTimeMillis()));
                        semaphore.acquire();
                        System.out.println(MessageFormat.format("{0} Acquired permit： 当前时间：「{1}」", Thread.currentThread().getName(), System.currentTimeMillis()));
                        Thread.sleep(100);
                        semaphore.release();
                        latch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        System.out.println("semaphore初始完毕");

        latch.await();

        System.out.println("latch.getCount() = " + latch.getCount());
    }

    @Test
    public void testCyclicBarrier() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            System.out.println("All parties have reached the barrier");
        });

        AtomicInteger atomicInteger = new AtomicInteger(1);

        CountDownLatch countDownLatch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(MessageFormat.format("{0} is waiting", Thread.currentThread().getName()));
                    try {
                        if (atomicInteger.decrementAndGet() == 0) {
                            System.out.println(MessageFormat.format("{0} is the last party", Thread.currentThread().getName()));
                            Thread.sleep(50000);
                        } else {
                            System.out.println(MessageFormat.format("{0} is waiting", Thread.currentThread().getName()));
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(MessageFormat.format("{0} has crossed", Thread.currentThread().getName()));
                    try {
                        barrier.await(1, TimeUnit.SECONDS);
                    } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }).start();

        }
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testBlockingQueue() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        AtomicBoolean running = new AtomicBoolean(true);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            BlockingQueue<Task> queue = new LinkedBlockingQueue<>(2);

            // 生产者
            executor.execute(() -> {
                while (running.get()) {
                    try {
                        // 准备生产数据
                        Task task = new Task();
                        Thread.sleep(100);
                        System.out.println("生产了数据");
                        queue.put(task); // 队列满了自动阻塞
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("生产者已停止，最后队列大小为：" + queue.size());
            });

            // 消费者
            executor.execute(() -> {
                while (running.get()) {
                    try {
                        System.out.println("正在消费数据");
                        countDownLatch.countDown();
                        Thread.sleep(100);
                        System.out.println("消费到了数据");
                        Task task = queue.take(); // 队列空了自动阻塞
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("消费者已停止，最后队列大小为：" + queue.size());
            });


            countDownLatch.await();
            running.compareAndSet(true, false);
            System.out.println("running = " + running);
            System.out.println("running.get() = " + running.get());

        }
    }

    public static class Task {
    }


    @Test
    public void testBlockingQueueToZero() throws InterruptedException {
        int targetConsumeCount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(targetConsumeCount);
        AtomicBoolean running = new AtomicBoolean(true);
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>(2);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 生产者：最多生产 targetConsumeCount 个任务
        executor.execute(() -> {
            int producedCount = 0;
            while (running.get() && producedCount < targetConsumeCount) {
                try {
                    Task task = new Task();
                    Thread.sleep(100); // 模拟生产耗时
                    queue.put(task);
                    producedCount++;
                    System.out.println("[生产者] 生产了第 " + producedCount + " / " + targetConsumeCount + " 个任务，当前队列大小：" + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("[生产者] 被中断，退出。已生产数量：" + producedCount);
                    break;
                }
            }
            // 生产完成后，主动将 running 设为 false，通知消费者（如果消费者还在等）
            running.set(false);
            System.out.println("[生产者] 已停止。总共生产了 " + producedCount + " 个任务，最终队列大小：" + queue.size());
        });

        // 消费者：消费 targetConsumeCount 个任务后停止
        executor.execute(() -> {
            int consumedCount = 0;
            while (consumedCount < targetConsumeCount) {
                try {
                    // 使用 poll 超时，避免无限阻塞
                    Task task = queue.poll(500, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        consumedCount++;
                        System.out.println("[消费者] 消费了第 " + consumedCount + " / " + targetConsumeCount + " 个任务，当前队列大小：" + queue.size());
                        countDownLatch.countDown();
                        // 模拟消费耗时
                        Thread.sleep(100);
                    } else {
                        // 没有任务，检查生产者是否已经停止
                        if (!running.get()) {
                            // 生产者已停止但队列为空，理论上不应该发生，因为生产者生产了足够数量
                            System.out.println("[消费者] 生产者已停止但队列为空，退出。已消费数量：" + consumedCount);
                            break;
                        }
                        System.out.println("[消费者] 暂时没有任务，等待中... 当前队列大小：" + queue.size());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("[消费者] 被中断，退出。已消费数量：" + consumedCount);
                    break;
                }
            }
            System.out.println("[消费者] 已停止。总共消费了 " + consumedCount + " 个任务，最终队列大小：" + queue.size());
        });

        // 等待消费者完成目标消费数量
        countDownLatch.await();
        System.out.println("========== 已达到目标消费数量（" + targetConsumeCount + "），准备停止线程池 ==========");
        // 确保生产者停止（可能已经自己停止了）
        running.set(false);
        // 关闭线程池，等待剩余任务完成（最多5秒）
        executor.shutdown();
        if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("线程池已正常终止，最终队列大小：" + queue.size());
        } else {
            System.out.println("线程池未完全终止，强制关闭。最终队列大小：" + queue.size());
            executor.shutdownNow();
        }

        // 最终断言：队列应该为空且消费数量等于目标
        assert queue.isEmpty();
    }
}


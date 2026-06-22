package cn.chenyunlong.qing.service.llm.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GC压力模拟工具
 * 用法：java GCPressure [options]
 * <p>
 * 选项：
 * -s <size>       每个对象大小（字节），默认 1024 (1KB)
 * -c <count>      每次分配的对象数量，默认 1000
 * -i <ms>         分配间隔（毫秒），默认 100
 * -l              开启内存泄漏（对象会被永久持有）
 * -t <seconds>    运行时间（秒），默认 60，0表示无限运行
 * -p              打印分配统计
 * -h              显示帮助
 */
public class GCPressure {

    private static boolean leak = false;
    private static int objectSize = 1024;       // bytes
    private static int batchCount = 1000;
    private static int intervalMs = 100;
    private static int runSeconds = 60;
    private static boolean printStats = false;

    private static final List<byte[]> leakStorage = new ArrayList<>();
    private static final AtomicLong totalAllocated = new AtomicLong(0);
    private static final AtomicLong totalReleased = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        parseArgs(args);
        if (runSeconds == 0) {
            System.out.println("Running indefinitely. Press Ctrl+C to stop.");
        } else {
            System.out.println("Running for " + runSeconds + " seconds.");
        }
        System.out.printf("Settings: objectSize=%d B, batchCount=%d, interval=%d ms, leak=%s\n",
            objectSize, batchCount, intervalMs, leak);

        long endTime = runSeconds > 0 ? System.currentTimeMillis() + runSeconds * 1000L : Long.MAX_VALUE;
        int iteration = 0;

        // 启动统计打印线程（可选）
        if (printStats) {
            Thread statsThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(5000);
                        System.out.printf("[Stats] Allocated: %d MB, Released: %d MB, LeakStorageSize: %d\n",
                            totalAllocated.get() / (1024 * 1024),
                            totalReleased.get() / (1024 * 1024),
                            leakStorage.size());
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            statsThread.setDaemon(true);
            statsThread.start();
        }

        while (System.currentTimeMillis() < endTime) {
            iteration++;
            allocateBatch();
            if (intervalMs > 0) {
                Thread.sleep(intervalMs);
            }
            if (iteration % 100 == 0 && !printStats) {
                System.out.print(".");
                if (iteration % 1000 == 0) System.out.println();
            }
        }

        System.out.println("\nFinished. Total allocated: " + totalAllocated.get() / (1024 * 1024) + " MB");
        if (leak) {
            System.out.println("Leak storage holds " + leakStorage.size() + " objects.");
        }
    }

    private static void allocateBatch() {
        long allocatedBytes = 0L;
        for (int i = 0; i < batchCount; i++) {
            byte[] obj = new byte[objectSize];
            // 填充一些随机值，防止JVM优化掉分配
            obj[0] = (byte) (i & 0xFF);
            obj[objectSize - 1] = (byte) ((i >> 8) & 0xFF);
            allocatedBytes += objectSize;

            if (leak) {
                leakStorage.add(obj);
            } else {
                // 不加泄漏时，让对象可被回收，但这里故意不保留引用，对象会立即变成垃圾
                // 为了模拟“快速分配+回收”的Young GC压力，不做额外操作
            }
        }
        totalAllocated.addAndGet(allocatedBytes);
        if (!leak) {
            totalReleased.addAndGet(allocatedBytes);
        }
    }

    private static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    objectSize = Integer.parseInt(args[++i]);
                    break;
                case "-c":
                    batchCount = Integer.parseInt(args[++i]);
                    break;
                case "-i":
                    intervalMs = Integer.parseInt(args[++i]);
                    break;
                case "-l":
                    leak = true;
                    break;
                case "-t":
                    runSeconds = Integer.parseInt(args[++i]);
                    break;
                case "-p":
                    printStats = true;
                    break;
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                default:
                    System.err.println("Unknown option: " + args[i]);
                    printHelp();
                    System.exit(1);
            }
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java GCPressure [options]");
        System.out.println("Options:");
        System.out.println("  -s <bytes>      object size in bytes, default 1024");
        System.out.println("  -c <count>      number of objects per allocation batch, default 1000");
        System.out.println("  -i <ms>         interval between batches (ms), default 100");
        System.out.println("  -l              enable memory leak (objects held forever)");
        System.out.println("  -t <seconds>    total run time (seconds), default 60, 0 = infinite");
        System.out.println("  -p              print allocation statistics every 5 seconds");
        System.out.println("  -h              this help");
    }
}

package cn.chenyunlong.qing.service.llm.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 模拟GC频繁的小程序
 * 用法：
 * java -Xms100m -Xmx100m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log GCPractice
 * 或使用G1：
 * java -Xms100m -Xmx100m -XX:+UseG1GC -XX:+PrintGCDetails -Xloggc:gc.log GCPractice
 */
public class GCPractice {

    // 开关：是否启用内存泄漏（累积对象）
    private static final boolean ENABLE_LEAK = false;  // true=模拟内存泄漏, false=只频繁GC不泄漏
    private static final int LEAK_SIZE = 1000;       // 每次添加多少个对象到泄漏列表

    // 模拟的业务对象
    static class Data {
        private byte[] payload;
        private long id;
        private String name;

        Data(long id) {
            this.id = id;
            this.name = "data_" + id;
            // 模拟实际业务中的对象大小，比如1KB
            this.payload = new byte[1024];
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== GC频繁模拟程序 ===");
        System.out.println("是否启用内存泄漏: " + (ENABLE_LEAK ? "是 (会导致Full GC/OOM)" : "否 (仅频繁Young GC)"));
        System.out.println("观察方式: jstat -gcutil <pid> 1000 或 查看gc.log");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println();

        List<Data> leakList = new ArrayList<>();  // 用于内存泄漏
        AtomicLong counter = new AtomicLong(0);

        // 持续运行，每轮创建一批对象
        while (true) {
            // 每轮创建1000个临时对象，这些对象很快失去引用，触发Young GC
            for (int i = 0; i < 1000; i++) {
                Data temp = new Data(counter.incrementAndGet());
                // 做一点简单操作，模拟业务处理
                if (temp.id % 10000 == 0) {
                    System.out.print(".");
                }
                // temp 在这个循环结束后即变为垃圾
            }

            // 如果启用内存泄漏，则把一部分对象添加到静态列表，让它们进入老年代并持续累积
            if (ENABLE_LEAK) {
                for (int i = 0; i < LEAK_SIZE; i++) {
                    leakList.add(new Data(counter.incrementAndGet()));
                }
                // 可选：定期打印泄漏列表大小
                if (leakList.size() % 10000 == 0) {
                    System.out.println("\n[泄漏] 当前累积对象数: " + leakList.size());
                }
            }

            // 稍微sleep一点，让其他线程有机会，但不影响GC压力；也可以注释掉让CPU跑满
            Thread.sleep(5);
        }
    }
}

/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Test1 {

    private static final ExecutorService service = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {

        final Map<String, Integer> map = new HashMap<String, Integer>(4);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            public void run() {
                Integer count1 = map.get("1");
                Integer count2 = map.get("2");
                Integer count3 = map.get("3");
                Integer count4 = map.get("4");
                System.out.println("count= " + (count1 + count2 + count3 + count4));
            }
        });
        new Thread(new TestThread(map, "1", cyclicBarrier, 1000)).start();
        new Thread(new TestThread(map, "2", cyclicBarrier, 1000)).start();
        new Thread(new TestThread(map, "3", cyclicBarrier, 1000)).start();
        new Thread(new TestThread(map, "4", cyclicBarrier, 1000)).start();

        //线程池实现方式
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            Future<Integer> data = service.submit(() -> {
                int sum1 = 0;
                for (int i1 = 0; i1 < 1000; i1++) {
                    sum1 += i1;
                }
                return sum1;
            });
            try {
                sum += data.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("count===" + sum);
    }

}

class TestThread implements Runnable {

    private final Map<String, Integer> map;
    private final String               type;
    private final CyclicBarrier        cb;
    private final int                  n;

    public TestThread(Map<String, Integer> map, String type, CyclicBarrier cyclicBarrier, int n) {
        this.map = map;
        this.type = type;
        this.cb = cyclicBarrier;
        this.n = n;
    }

    @Override
    public void run() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        map.put(type, sum);
        try {
            cb.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

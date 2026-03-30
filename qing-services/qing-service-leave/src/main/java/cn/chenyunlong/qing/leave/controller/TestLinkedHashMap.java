package cn.chenyunlong.qing.leave.controller;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestLinkedHashMap<T, K> extends LinkedHashMap<T, K> {

    TestLinkedHashMap(int initialCapacity,
                      boolean accessOrder) {
        super(initialCapacity, 0.75f, false);
    }

    private static final int MAX_CAPACITY = 4;

    @Override
    protected boolean removeEldestEntry(Map.Entry<T, K> eldest) {
        return size() > MAX_CAPACITY;
    }

    public static void main(String[] args) {
        TestLinkedHashMap<String, String> hashMap = new TestLinkedHashMap<>(4, true);

        hashMap.put("1", "1");
        hashMap.put("2", "2");
        hashMap.put("3", "3");
        hashMap.put("4", "4");
        hashMap.put("5", "5");
        hashMap.put("6", "6");
        System.out.println("hashMap.containsKey(\"3\") = " + hashMap.containsKey("3"));
        hashMap.put("7", "7");
        System.out.println("hashMap.containsKey(\"3\") = " + hashMap.containsKey("3"));

        hashMap.entrySet().forEach(System.out::println);

        Map.Entry<String, String> stringEntry = hashMap.lastEntry();

        Map.Entry<String, String> stringEntry1 = hashMap.pollLastEntry();

        System.out.println("stringEntry1.getKey() = " + stringEntry1.getKey());

        System.out.println("poll之后");

        hashMap.entrySet().forEach(System.out::println);
    }
}

package cn.chenyunlong.qing.auth.application.service;

import java.util.*;

public class RetainAllExample {
    public static void main(String[] args) {
        // 示例1：基本用法
        List<String> list1 = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        List<String> list2 = Arrays.asList("C", "D", "E", "F", "G");


        boolean b = list1.removeAll(list2);

        System.out.println("b = " + list1);


        boolean changed = list1.retainAll(list2);
        System.out.println("是否改变: " + changed);  // true
        System.out.println("交集结果: " + list1);    // [C, D, E]
        System.out.println();

        // 示例2：没有改变的情况
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));

        changed = set1.retainAll(set2);
        System.out.println("是否改变: " + changed);  // false（因为set1已经是set2的子集）
        System.out.println("结果: " + set1);         // [1, 2, 3]
        System.out.println();

        // 示例3：与空集合求交集
        List<String> list3 = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
        List<String> emptyList = new ArrayList<>();

        changed = list3.retainAll(emptyList);
        System.out.println("是否改变: " + changed);  // true
        System.out.println("与空集交集: " + list3);  // []（清空了所有元素）
        System.out.println();

        // 示例4：不同类型集合之间
        List<String> list4 = new ArrayList<>(Arrays.asList("Apple", "Banana", "Orange"));
        Set<String> set4 = new HashSet<>(Arrays.asList("Banana", "Grape"));

        changed = list4.retainAll(set4);
        System.out.println("List与Set的交集: " + list4);  // [Banana]
    }
}

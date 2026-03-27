package cn.chenyunlong.qing.leave.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {

    private final static Map<String, Integer> mapping;

    static {
        mapping = new HashMap<>();

        for (int i = 2; i < 10; i++) {
            mapping.put(String.valueOf(i), i);
        }
        mapping.put("A", 1);
        mapping.put("J", 11);
        mapping.put("Q", 12);
        mapping.put("K", 13);
    }

    public static void main(String[] args) {
        Scanner Scanner = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String input = Scanner.nextLine();
        if (input.contains("jocker") || input.contains("JOCKER")) {
            System.out.println("ERROR");
            return;
        }

        String[] strs = input.split(" ");
        List<String> strings = new ArrayList<>(strs.length);

        for (int i = 0; i < strs.length; i++) {
            strings.add(strs[i]);
        }

        for (int i = 0; i < strings.size(); i++) {
            String result = compute(24, strings);

            if (result != null) {
                System.out.println(result);
                return;
            }
        }
        System.out.println("NONE");
    }

    // 递归计算
    public static String compute(Integer total, List<String> numbers) {

        String result = null;

        String currentNumber = numbers.get(0);
        List<String> sublist;

        if (numbers.size() > 1) {
            sublist = numbers.subList(1, numbers.size());
        } else {
            sublist = new ArrayList<>();
        }

        Integer value = mapping.get(currentNumber);

        // 分别计算 + - * /

        // 计算加法
        Integer nextTotal = total - value;

        if (sublist.isEmpty()) {

        } else {

            String computResult = compute(nextTotal, sublist);

            if (computResult != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(currentNumber);
                sb.append("+").append(computResult);
                return sb.toString();
            }
        }

        // 计算减法


        return result;
    }

}

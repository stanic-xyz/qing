package cn.chenyunlong.qing.leave.controller;

import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String str = in.nextLine().trim();

        int length = str.length();

        int turns = length / 8;
        int total = 0;

        for (int i = 0; i < turns; i++) {
            total += 8;
            System.out.println(str.substring((i) * 8, (i + 1 ) * 8));
        }

        if (total < length) {
            StringBuilder sb = new StringBuilder();
            sb.append(str, total, length);

            for (int j = 0; j < 8-(length - total); j++) {
                sb.append("0");
            }
            System.out.println(sb.toString());
        }
    }
}

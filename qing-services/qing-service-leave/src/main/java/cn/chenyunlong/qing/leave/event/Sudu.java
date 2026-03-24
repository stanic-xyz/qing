package cn.chenyunlong.qing.leave.event;

import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Sudu {

    public static class Point {
        public int value;
        public  boolean line_status = false;
        public  boolean colStatus = false;
        public  boolean filled = false;
        public int x;
        public int y;

        public Point() {
        }

        public Point(int value, int x, int y) {
            if (value > 9 || value < 0) {
                throw new IllegalArgumentException("参数错误，数据范围0~9");
            }
            Point point = new Point();

            if (value != 0) {
                point.value = value;
                point.filled = true;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        Point[][] data = new Point[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Point point = new Point(in.nextInt(), i, j);
                data[i][j] = point;
            }
        }
        // 检查状态-》填充数据 循环
        solve(data, 0, 0);

        for (int i = 0; i < 9; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                sb.append(data[i][j].value).append(" ") ;
            }
            System.out.println(sb.toString());
        }

    }

    static boolean solve(Point[][] board, int i, int j) {
        if (i == 9) return true; // 所有行处理完
        if (j == 9) return solve(board, i + 1, 0); // 下一行
        if (board[i][j].filled) return solve(board, i, j + 1); // 已填则跳过

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, i, j, num)) { // 检查行、列、宫
                board[i][j].value = num;
                board[i][j].filled = true;
                if (solve(board, i, j + 1)) return true;
                board[i][j].filled = false; // 回溯
            }
        }
        return false; // 无解
    }

    // 检查状态
    private static boolean isValid(Point[][] data, int x, int y, int num) {

        for (int j = 0; j < 9; j++) {
            Point point = data[x][j];
            if (!point.filled ) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            Point point = data[i][y];
            if (!point.filled ) {
                return false;
            }

        }

        return true;
    }

    public static boolean check_tangle(Point[][] data, int x, int y) {

        int x_start = 0;
        int x_end = 0;
        int y_start = 0;
        int y_end = 0;

        if (x == 0) {
            x_start = 0;
            x_end = 2;
        }

        if (x == 8) {
            x_start = 6;
            x_end = 8;
        }

        for (int i = x_start; i < x_end + 1; i++) {
            for (int j = y_start; j < y_start; j++) {
                Point point = data[i][j];
                if (!point.filled ) {
                    return false;
                }
            }
        }

        return false;
    }
}

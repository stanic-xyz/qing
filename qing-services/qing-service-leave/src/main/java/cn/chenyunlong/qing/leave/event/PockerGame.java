package cn.chenyunlong.qing.leave.event;

import java.util.Scanner;
import java.util.*;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class PockerGame {

    public static final Map<String, Integer> weight_mapping;

    static {
        weight_mapping = new HashMap<>();
        for (int i = 3; i < 10; i++) {
            weight_mapping.put(i + "", i);
        }
    }

    public enum PockerType {
        Single,
        Pair,
        Order,
        Boom,
        JockerBoom
    }

    public static class Types {

        public String value;
        public PockerType type;
        public int level = 0;

        public Types(String value) {

            String[] singples = value.split(" ");

            if (singples.length == 1) {
                this.type = PockerType.Single;
                this.value = value;
            }

            // 对子 或者 双王
            if (singples.length == 2) {
                if (singples[0] == "joker" || singples[1] == "joker") {
                    this.type = PockerType.JockerBoom;
                    this.value = value;
                } else {
                    this.type = PockerType.Single;
                    this.value = value;
                }
            }
        }

        public String compare(Types other) {
            switch (this.type) {
                // 如果是王炸，直接判断当前最大，不可能出现两边都是王炸把！
                case JockerBoom:
                    return this.value;
                // 当前为炸弹，只有对面为炸弹或者王炸的时候才有效
                case Boom:
                    return other.type == PockerType.JockerBoom ? other.value : compareboom(this,
                            other);


            }
            return other.value;
        }

        private String compareboom(Types current, Types other) {
            return other.value;
        }
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String input_value = in.nextLine();

        String[] pair = input_value.split("-");

        if (pair.length != 2) {
            System.out.println("ERROR");
            return;
        }

        Types types1 = new Types(pair[0]);
        Types types2 = new Types(pair[1]);


        System.out.println(types1.compare(types2));

    }
}

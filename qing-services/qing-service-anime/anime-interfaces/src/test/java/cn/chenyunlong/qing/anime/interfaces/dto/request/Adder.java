package cn.chenyunlong.qing.anime.interfaces.dto.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Adder {
}

class Solution {
    public int[] twoSum(int[] nums, int target) {

        Map<Integer, Integer> resultMap = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {

            int targetNumber = target - nums[i];

            if (resultMap.containsKey(targetNumber)) {
                return new int[]{resultMap.get(target - nums[i]), i};
            } else {
                resultMap.put(nums[i], i);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] ints = new Solution().twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println("ints = " + Arrays.toString(ints));
    }
}

package cn.chenyunlong.qing.service.llm.thread;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 字节OD笔试题
 */
public class TestByteDanceTest {

    /**
     * 1.只出现一次的数字
     * 某个元素只出现一次以外，其余每个元素均出现两次，找出1次的
     * 输入：nums = [2,2,1]
     * 输出：1
     *
     */
    @Test
    public void testMain() {
        String[] nums = {"2", "2", "1"};
        Set<String> set = new HashSet<>();
        for (String arg : nums) {
            if (set.contains(arg)) {
                set.remove(arg);
                continue;
            }
            set.add(arg);
        }
        System.out.println(set.stream().findFirst().orElse(null));
    }


    /**
     * 2.识别有效括号
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。有效字符串需满足：
     * 1. 左括号必须用相同类型的右括号闭合。
     * 2. 左括号必须以正确的顺序闭合。
     * 3. 每个右括号都有一个对应的相同类型的左括号。
     * 示例 1：
     * 输入：s = ""()""
     * 输出：true
     * 示例 2：
     * 输入：s = ""()[]{}""
     * 输出：true
     * 示例 3：
     * 输入：s = ""(]""
     * 输出：false
     */
    @Test
    public void testQuoteValidation() {
        String args = "()[]{}";

        // 使用Map存储括号对应关系
        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('{', '}');
        map.put('[', ']');

        // 使用Set存储左括号
        Set<Character> startSet = new HashSet<>();
        startSet.add('(');
        startSet.add('{');
        startSet.add('[');


        Stack<Character> stack = new Stack<>();

        boolean result = true;

        for (int i = 0; i < args.length(); i++) {
            char c = args.charAt(i);
            if (startSet.contains(c)) {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    result = false;
                    break;
                }
                char top = stack.pop();
                if (!Objects.equals(map.get(top), c)) {
                    result = false;
                    break;
                }
            }
        }

        System.out.println(result);
    }


    /**
     * 3.删除链表中倒数第 N 个节点
     * 示例1：
     * 输入：head = [1,2,3,4,5], n = 2
     * 输出：[1,2,3,5]
     * 示例2：
     * 输入：head = [1], n = 1
     * 输出：[]
     * 示例3：
     * 输入：head = [1,2], n = 1
     * 输出：[1]
     */

    @Test
    public void testBackDeleteElement() {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);

        // 删除倒数第2个节点
        backDelete(head, 2);

        printLinkedList(head);
    }

    private void printLinkedList(Node head) {
        StringBuilder sb = new StringBuilder();
        Node cur = head;
        while (cur != null) {
            sb.append(cur.value);
            cur = cur.next;
        }
        System.out.println(sb);
    }

    /**
     * 删除倒数第n个节点
     *
     * @param head 链表头
     * @param n    倒数第n个节点
     */
    private void backDelete(Node head, int n) {
        if (head == null || n <= 0) return;
        Node dummy = new Node(0);
        dummy.next = head;
        Node fast = dummy, slow = dummy;
        // 快指针先走 n 步
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        // 快慢指针同时移动，直到快指针到达最后一个节点
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        // 删除 slow 的下一个节点
        slow.next = slow.next.next;
    }

    public static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }
}

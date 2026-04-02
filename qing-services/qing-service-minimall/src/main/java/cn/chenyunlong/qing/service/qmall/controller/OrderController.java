package cn.chenyunlong.qing.service.qmall.controller;

import cn.chenyunlong.qing.service.qmall.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestParam("productId") Long productId, @RequestParam("userId") Long userId) {
        boolean success = orderService.createOrder(productId, userId);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "下单成功");
        } else {
            result.put("code", 500);
            result.put("message", "库存不足");
        }
        return result;
    }
}

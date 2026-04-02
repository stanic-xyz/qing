package cn.chenyunlong.qing.service.qmall.service;

import cn.chenyunlong.qing.service.qmall.entity.Order;
import cn.chenyunlong.qing.service.qmall.entity.Product;
import cn.chenyunlong.qing.service.qmall.model.OrderMessage;
import cn.chenyunlong.qing.service.qmall.repository.OrderRepository;
import cn.chenyunlong.qing.service.qmall.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final StringRedisTemplate redisTemplate;

    private final RedisScript<Long> stockScript;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private static final String STOCK_KEY_PREFIX = "stock:product:";
    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createOrder(Long productId, Long userId) {
        String stockKey = STOCK_KEY_PREFIX + productId;

        // 1. Redis 原子扣减库存
        Long result = redisTemplate.execute(
                stockScript,
                Collections.singletonList(stockKey),
                "1"
        );
        if (result == 0) {
            log.warn("Redis库存不足，productId={}", productId);
            return false;
        }

        // 2. 生成订单号
        String orderNo = UUID.randomUUID().toString().replace("-", "");

        // 3. 插入订单（先插入，后续如果库存更新失败可回滚）
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setOrderNo(orderNo);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        orderRepository.save(order);

        // 4. 更新数据库库存（乐观锁）
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        if (product.getStock() <= 0) {
            // 理论上 Redis 已扣减，但数据库库存可能不足，需回滚 Redis
            rollbackRedisStock(productId);
            throw new RuntimeException("数据库库存不足，回滚Redis");
        }

        kafkaTemplate.send("order-topic", "stanic", OrderMessage.builder()
                .orderNo(orderNo)
                .userId(userId)
                .productId(product.getId())
                .build());

        // 更新库存（JPA 自动使用 @Version 进行乐观锁检查）
        product.setStock(product.getStock() - 1);
        try {
            productRepository.save(product);
        } catch (Exception e) {
            // 乐观锁冲突或更新失败，回滚 Redis
            rollbackRedisStock(productId);
            throw new RuntimeException("数据库库存更新失败，回滚Redis", e);
        }

        return true;
    }

    private void rollbackRedisStock(Long productId) {
        String stockKey = STOCK_KEY_PREFIX + productId;
        redisTemplate.opsForValue().increment(stockKey, 1);
        log.warn("回滚Redis库存，productId={}", productId);
    }
}

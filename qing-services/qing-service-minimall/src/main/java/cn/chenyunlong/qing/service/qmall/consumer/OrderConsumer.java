package cn.chenyunlong.qing.service.qmall.consumer;

import cn.chenyunlong.qing.service.qmall.entity.Order;
import cn.chenyunlong.qing.service.qmall.entity.Product;
import cn.chenyunlong.qing.service.qmall.model.OrderMessage;
import cn.chenyunlong.qing.service.qmall.repository.OrderRepository;
import cn.chenyunlong.qing.service.qmall.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
@AllArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "order-topic", concurrency = "3")
    public void consume(OrderMessage message, Acknowledgment ack) {
        try {
            // 1. 插入订单
            Order order = new Order();
            order.setUserId(message.getUserId());
            order.setProductId(message.getProductId());
            order.setOrderNo(message.getOrderNo());
            order.setStatus(0);
            order.setCreateTime(LocalDateTime.now());
            orderRepository.save(order);

            // 2. 更新数据库库存（乐观锁）
            Product product = productRepository.findById(message.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));
            if (product.getStock() <= 0) {
                // 理论上 Redis 已扣，这里可能是数据异常，记录并回滚
                log.error("数据库库存不足，productId={}", message.getProductId());
                // 可选：发送补偿消息回滚 Redis
                throw new RuntimeException("数据库库存不足");
            }
            product.setStock(product.getStock() - 1);
            productRepository.save(product);  // JPA 自动使用 @Version 乐观锁

            // 3. 手动提交 offset
            ack.acknowledge();
        } catch (Exception e) {
            log.error("处理订单消息失败，将重试", e);
            // 不提交 offset，消息会重试（需配置重试策略）
            throw new RuntimeException(e);
        }
    }
}

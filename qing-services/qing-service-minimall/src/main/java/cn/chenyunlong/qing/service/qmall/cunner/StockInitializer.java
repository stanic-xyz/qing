package cn.chenyunlong.qing.service.qmall.cunner;

import cn.chenyunlong.qing.service.qmall.entity.Product;
import cn.chenyunlong.qing.service.qmall.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StockInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient; // 注入 RedissonClient

    public StockInitializer(ProductRepository productRepository,
                            StringRedisTemplate redisTemplate,
                            RedissonClient redissonClient) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 尝试获取分布式锁，锁的 key 可以是 "stock:init:lock"
        RLock lock = redissonClient.getLock("stock:init:lock");
        try {
            // 尝试加锁，最多等待 5 秒，锁自动释放时间 30 秒
            if (lock.tryLock(5, 30, TimeUnit.SECONDS)) {
                // 1. 检查 Redis 中是否已有库存数据（可根据业务定义）
                String hasInit = redisTemplate.opsForValue().get("stock:init:flag");
                if ("true".equals(hasInit)) {
                    log.info("库存数据已存在，跳过初始化");
                    return;
                }

                // 2. 执行真正的初始化逻辑
                List<Product> products = productRepository.findAll();
                if (products.isEmpty()) {
                    // 插入测试数据（注意：这里仍可能重复，建议数据库唯一约束）
                    Product product = new Product();
                    product.setName("测试商品");
                    product.setStock(100);
                    productRepository.save(product);
                    products = List.of(product);
                }

                // 3. 只设置缺失的库存键，避免覆盖已有值
                for (Product product : products) {
                    String key = "stock:product:" + product.getId();
                    Boolean hasKey = redisTemplate.hasKey(key);
                    if (Objects.equals(Boolean.FALSE, hasKey)) {
                        redisTemplate.opsForValue().set(key, String.valueOf(product.getStock()));
                    }
                }

                // 4. 设置初始化完成标志，后续实例不再执行
                redisTemplate.opsForValue().set("stock:init:flag", "true", Duration.ofHours(24));
                log.info("库存初始化完成");
            } else {
                log.info("其他实例正在执行初始化，本实例跳过");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取分布式锁被中断", e);
        } catch (Exception e) {
            log.error("初始化库存失败", e);
        } finally {
            // 释放锁（如果当前线程还持有）
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

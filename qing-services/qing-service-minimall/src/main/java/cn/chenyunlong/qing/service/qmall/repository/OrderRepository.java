package cn.chenyunlong.qing.service.qmall.repository;

import cn.chenyunlong.qing.service.qmall.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

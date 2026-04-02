package cn.chenyunlong.qing.service.qmall.service;

import org.springframework.transaction.annotation.Transactional;

public interface OrderService {

    boolean createOrder(Long productId, Long userId);
}

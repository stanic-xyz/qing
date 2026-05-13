package cn.chenyunlong.qing.leave.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    // 使用数据库默认级别
    @Transactional
    public void createOrder() {}

    // 指定读已提交
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void queryOrder() {}

    // 指定可重复读
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateStock() {}
}

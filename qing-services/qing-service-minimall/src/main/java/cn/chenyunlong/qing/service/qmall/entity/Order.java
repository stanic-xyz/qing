package cn.chenyunlong.qing.service.qmall.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_no", unique = true)
    private String orderNo;

    private Integer status;   // 0-待支付，1-已支付，2-取消

    @Column(name = "create_time")
    private LocalDateTime createTime;
}

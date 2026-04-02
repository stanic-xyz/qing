package cn.chenyunlong.qing.service.qmall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class OrderMessage {
    private Long productId;
    private Long userId;
    private String orderNo;
    private Long timestamp;
}

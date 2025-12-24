package cn.chenyunlong.codegen.example.domain;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单实体
 */
@Getter
@Setter
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenRepository
@GenService
@GenServiceImpl
@GenController
@GenMapper
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
public class Order extends BaseSimpleBusinessEntity<OrderId> {

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单金额
     */
    private Double amount;

    /**
     * 订单状态
     */
    private String status;
}

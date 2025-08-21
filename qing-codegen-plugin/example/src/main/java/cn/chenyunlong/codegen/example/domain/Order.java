package cn.chenyunlong.codegen.example.domain;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
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
public class Order extends BaseAggregate {

    /**
     * ID
     */
    private Long id;

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
package cn.chenyunlong.qing.anime.domain.product;

import cn.chenyunlong.codegen.annotation.GenController;
import cn.chenyunlong.codegen.annotation.GenCreator;
import cn.chenyunlong.codegen.annotation.GenQuery;
import cn.chenyunlong.codegen.annotation.GenRepository;
import cn.chenyunlong.codegen.annotation.GenService;
import cn.chenyunlong.codegen.annotation.GenUpdater;
import cn.chenyunlong.codegen.annotation.GenVo;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 产品实体
 */
@Entity
@Table(name = "product")
@Data
@EqualsAndHashCode(callSuper = true)
@GenRepository
@GenService
@GenController
@GenCreator
@GenUpdater
@GenQuery
@GenVo
public class Product extends BaseAggregate {

    /**
     * 产品名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 产品描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 产品价格
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 库存数量
     */
    @Column(name = "stock", nullable = false)
    private Integer stock;
}
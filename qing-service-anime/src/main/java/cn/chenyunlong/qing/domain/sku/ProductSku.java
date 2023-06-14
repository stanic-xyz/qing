/*
 * Copyright (c) 2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.sku;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.BitFlag;
import cn.chenyunlong.jpa.support.converter.BitFlagConverter;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import cn.hutool.core.util.NumberUtil;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "product_sku")
@Data
@GenVo(pkgName = "com.shutu.adminsrv.domain.sku.vo")
@GenDto(pkgName = "com.shutu.adminsrv.domain.sku.dto")
@GenUpdater(pkgName = "com.shutu.adminsrv.domain.sku.updater")
@GenQuery(pkgName = "com.shutu.adminsrv.domain.sku.query")
public class ProductSku extends BaseJpaAggregate {
    @Column(name = "product_id")
    private Long productId;

    private Integer sortNum = 0;

    @FieldDesc(name = "规格唯一编码")
    private String specCode;

    @QueryItem
    private String skuName;

    private BigDecimal price;

    @FieldDesc(name = "显示价格")
    private BigDecimal displayPrice;

    @Convert(converter = ValidStatusConverter.class)
    @IgnoreUpdater
    @IgnoreDto
    @QueryItem
    private ValidStatus validStatus;

    @Convert(converter = BitFlagConverter.class)
    private BitFlag onlineFlag;

    @FieldDesc(name = "是否自动发货")
    @Enumerated(EnumType.STRING)
    private BitFlag autoSend;

    @Enumerated(EnumType.STRING)
    private BitFlag grantFlag;

    @Enumerated(EnumType.STRING)
    private GrantTimeType timeType;

    @FieldDesc(name = "授权时间")
    private Integer grantTime;

    @Enumerated(EnumType.STRING)
    @QueryItem
    private BitFlag freeFlag;

    @FieldDesc(name = "是否为会员所有")
    @Enumerated(EnumType.STRING)
    private BitFlag memberFlag;

    @Enumerated(EnumType.STRING)
    @QueryItem
    private ProductType type;

    private String picUrl;

    private String bigPicUrl;

    @QueryItem
    private String subject;

    @QueryItem
    private String category;

    @Convert(converter = BitFlagConverter.class)
    private BitFlag padShow;

    @Convert(converter = BitFlagConverter.class)
    private BitFlag webShow;


    public void init() {
        if (NumberUtil.isGreater(getPrice(), new BigDecimal(0))) {
            setFreeFlag(BitFlag.N);
        } else {
            setFreeFlag(BitFlag.Y);
        }
        setPadShow(BitFlag.Y);
        setWebShow(BitFlag.Y);
    }

    public void online() {
        setOnlineFlag(BitFlag.Y);
    }

    public void offline() {
        setOnlineFlag(BitFlag.N);
    }

    public void padShow() {
        setPadShow(BitFlag.Y);
    }

    public void padOff() {
        setPadShow(BitFlag.N);
    }

    public void webShow() {
        setWebShow(BitFlag.Y);
    }

    public void webOff() {
        setWebShow(BitFlag.N);
    }

}

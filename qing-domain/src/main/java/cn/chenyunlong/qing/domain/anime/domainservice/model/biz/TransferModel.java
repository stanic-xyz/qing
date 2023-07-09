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

package cn.chenyunlong.qing.domain.anime.domainservice.model.biz;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TransferModel {

    @Schema(name = "skuId")
    private Long skuId;

    @Schema(name = "操作用户")
    private String operateUser;

    @Schema(name = "转入仓库id")
    private Long transferInHouseId;

    @Schema(name = "转出仓库id")
    private Long transferOutHouseId;

    @Schema(name = "唯一编码")
    private List<Long> uniqueCodes;

    @Schema(name = "批次号")
    private String batchNo;

}

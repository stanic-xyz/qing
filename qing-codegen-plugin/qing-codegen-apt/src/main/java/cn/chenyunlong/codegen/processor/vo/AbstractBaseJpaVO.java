/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.codegen.processor.vo;

import com.only4play.jpa.support.BaseJpaAggregate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AbstractBaseJpaVO {

    @Schema(title = "数据版本")
    private int version;

    @Schema(title = "主键")
    private Long id;

    @Schema(title = "创建时间")
    private Long createdAt;

    @Schema(title = "修改时间")
    private Long updatedAt;

    protected AbstractBaseJpaVO(BaseJpaAggregate source) {
        this.setVersion(source.getVersion());
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt().toEpochMilli());
        this.setUpdatedAt(source.getUpdatedAt().toEpochMilli());
    }

    protected AbstractBaseJpaVO() {
    }
}

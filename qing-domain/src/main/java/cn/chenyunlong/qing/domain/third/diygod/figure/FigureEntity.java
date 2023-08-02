/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.domain.third.diygod.figure;

import cn.chenyunlong.jpa.support.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 手办模型 DIYGod
 *
 * @author Stan
 * @date 2022/11/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "figure")
@Entity
public class FigureEntity extends BaseEntity {
    private String name;
    private String otherNames;
    private LocalDate releaseDate;
    private String characterName;
    private String relatedWork;
    private String producer;
    private BigDecimal price;
    private String links;
    private String coverImg;
}

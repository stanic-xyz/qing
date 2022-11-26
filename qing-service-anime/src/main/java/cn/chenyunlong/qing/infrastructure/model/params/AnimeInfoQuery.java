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

package cn.chenyunlong.qing.infrastructure.model.params;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.infrastructure.model.dto.base.InputConverter;
import cn.chenyunlong.qing.infrastructure.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeInfoQuery implements InputConverter<AnimeInfo> {

    private String keyword = "";
    private String district = "all";
    private String name = "";
    private String version = "all";
    private String year = "all";
    private String status = "all";
    private String type = "all";
    private Integer seasonMonth = -1;
    private String resourceType = "all";
    private String sort = "premiere_date";

    public LocalDate getStartYear() {
        if (StringUtils.isNotBlank(getYear()) && !"all".equals(getYear())) {
            String[] strings = getYear().split(",");
            return LocalDate.of(Integer.parseInt(strings[0].replace("[", "")), 1, 1);
        }
        return LocalDate.now().plusYears(-100);
    }

    public LocalDate getEndYear() {
        if (StringUtils.isNotBlank(getYear()) && !"all".equals(getYear())) {
            String[] strings = getYear().split(",");
            return LocalDate.of(Integer.parseInt(strings[1].replace(")", "")), 12, 31);
        }
        return LocalDate.now();
    }
}

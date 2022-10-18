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

package cn.chenyunlong.qing.model.vo;

import cn.chenyunlong.qing.model.entities.AnimeType;
import cn.chenyunlong.qing.model.entities.District;
import cn.chenyunlong.qing.model.entities.Version;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class OptionsModel {
    private List<AnimeType> typeList;
    private List<Version> versionList;
    private List<District> districtList;
    private List<String> indexList;
    private List<YearInfo> years;
}

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

package cn.chenyunlong.qing.mapper;

import cn.chenyunlong.qing.model.entities.AnimeRecommendEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Stan
 * @date 2020/01/26
 */
@Mapper
public interface AnimeRecommendMapper extends BaseMapper<AnimeRecommendEntity> {
    /**
     * 获取所有版本信息
     *
     * @return 版本信息
     */
    List<AnimeRecommendEntity> getAllVersions();

    /**
     * @param version 版本信息
     */
    void addVersionInfo(AnimeRecommendEntity version);

}
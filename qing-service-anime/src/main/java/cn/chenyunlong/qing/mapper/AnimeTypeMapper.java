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

import cn.chenyunlong.qing.model.entities.AnimeType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 */
@Mapper
@Component
public interface AnimeTypeMapper extends BaseMapper<AnimeType> {
    /**
     * 获取所有类型
     *
     * @return 所有类型信息
     */
    List<AnimeType> listTypes();

    /**
     * 根据ID获取类型详情
     *
     * @param id 类型ID
     * @return 类型信息
     */
    AnimeType selectTypeById(@Param("id") Long id);

    /**
     * 添加类型信息
     *
     * @param animeType 动漫类型
     */
    void addAnimeType(AnimeType animeType);

    /**
     * 删除动漫类型信息
     *
     * @param animeType 动漫类型信息
     */
    void deleteAnimeType(AnimeType animeType);
}

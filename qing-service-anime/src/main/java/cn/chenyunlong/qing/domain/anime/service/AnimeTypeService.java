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

package cn.chenyunlong.qing.domain.anime.service;

import cn.chenyunlong.qing.domain.anime.AnimeType;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeTypeParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeTypeService {
    /**
     * @return 获取动漫类型信息
     */
    List<AnimeType> getAllTypeInfo();

    /**
     * @param animeType 动漫类型
     * @return 动漫信息
     */
    AnimeType addAnimeType(AnimeType animeType);

    /**
     * 获取动漫类型详情
     *
     * @param typeId 动漫类型ID
     * @return 动漫类型信息
     */
    AnimeType getById(Long typeId);

    /**
     * 更新动漫信息
     *
     * @param typeInfo 类型信息
     * @return 更新后的类型信息
     */
    AnimeType update(AnimeType typeInfo);

    /**
     * 分页获取类型信息
     *
     * @param typeParam 类型参数信息
     * @param pageable  分页信息
     * @return 分页信息
     */
    IPage<AnimeType> pageBy(AnimeTypeParam typeParam, Pageable pageable);
}

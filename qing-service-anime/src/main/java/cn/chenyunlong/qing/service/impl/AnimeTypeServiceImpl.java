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

package cn.chenyunlong.qing.service.impl;

import cn.chenyunlong.qing.mapper.AnimeTypeMapper;
import cn.chenyunlong.qing.model.entities.AnimeType;
import cn.chenyunlong.qing.model.params.AnimeTypeParam;
import cn.chenyunlong.qing.service.AnimeTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeTypeServiceImpl implements AnimeTypeService {
    private final AnimeTypeMapper animeTypeMapper;

    public AnimeTypeServiceImpl(AnimeTypeMapper animeTypeMapper) {
        this.animeTypeMapper = animeTypeMapper;
    }

    @Override
    public List<AnimeType> getAllTypeInfo() {
        return animeTypeMapper.listTypes();
    }

    @Override
    public AnimeType addAnimeType(AnimeType animeType) {
        animeType.preCheck();
        animeTypeMapper.addAnimeType(animeType);
        return animeType;
    }

    @Override
    public AnimeType getById(Long typeId) {
        return animeTypeMapper.selectById(typeId);
    }

    @Override
    public AnimeType update(AnimeType typeInfo) {
        animeTypeMapper.updateById(typeInfo);
        return typeInfo;
    }

    @Override
    public IPage<AnimeType> pageBy(AnimeTypeParam typeParam, Pageable pageable) {
        Page<AnimeType> animeTypePage = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        LambdaQueryWrapper<AnimeType> queryWrapper = new LambdaQueryWrapper<>();
        return animeTypeMapper.selectPage(animeTypePage, queryWrapper);
    }
}

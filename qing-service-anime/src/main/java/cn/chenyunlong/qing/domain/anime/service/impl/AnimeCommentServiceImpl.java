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

package cn.chenyunlong.qing.domain.anime.service.impl;

import cn.chenyunlong.qing.domain.anime.service.AnimeCommentService;
import cn.chenyunlong.qing.domain.comment.AnimeComment;
import cn.chenyunlong.qing.domain.comment.mapper.AnimeCommentMapper;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeCommentDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeCommentServiceImpl implements AnimeCommentService {

    private final AnimeCommentMapper commentMapper;

    public AnimeCommentServiceImpl(AnimeCommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public IPage<AnimeCommentDTO> getCommentsByAnimeId(Long animeId, int page, int pageSize) {
        QueryWrapper<AnimeComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", animeId);
        return convertToDTO(commentMapper.selectPage(new Page<>(page, pageSize), queryWrapper));
    }

    private IPage<AnimeCommentDTO> convertToDTO(IPage<AnimeComment> commentPage) {
        Assert.notNull(commentPage, "comments page must not be null");

        // Convert
        List<AnimeCommentDTO> commentDTOPage = commentPage.getRecords().stream().map(comment
                -> (AnimeCommentDTO) new AnimeCommentDTO().convertFrom(comment)).collect(Collectors.toList());

        // Build and return
        Page<AnimeCommentDTO> page = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        page.setRecords(commentDTOPage);
        return page;
    }
}

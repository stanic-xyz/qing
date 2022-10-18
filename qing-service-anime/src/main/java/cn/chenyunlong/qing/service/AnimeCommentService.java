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

package cn.chenyunlong.qing.service;

import cn.chenyunlong.qing.model.dto.AnimeCommentDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;


public interface AnimeCommentService {
    /**
     * 获取动漫平均信息
     *
     * @param animeId  动漫ID
     * @param page     当前页
     * @param pageSize 分页大小
     * @return 动漫信息（包含分页信息）
     */
    IPage<AnimeCommentDTO> getCommentsByAnimeId(Long animeId, int page, int pageSize);
}

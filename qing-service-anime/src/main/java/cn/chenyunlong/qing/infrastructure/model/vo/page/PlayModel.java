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

package cn.chenyunlong.qing.infrastructure.model.vo.page;

import cn.chenyunlong.qing.domain.anime.model.anime.AnimeInfoPlayVo;
import cn.chenyunlong.qing.domain.anime.response.AnimeInfoMinimalDTO;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeCommentDTO;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeEpisodeDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayModel extends BaseModel {

    private AnimeInfoPlayVo animeInfo;
    private List<AnimeInfoMinimalDTO> relevant;
    private List<AnimeInfoMinimalDTO> recommendation;
    private IPage<AnimeCommentDTO> comments;
    private AnimeEpisodeDTO episodeInfo;
}

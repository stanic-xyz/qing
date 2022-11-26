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

import cn.chenyunlong.qing.domain.anime.AnimeEpisodeEntity;
import cn.chenyunlong.qing.domain.anime.PlaylistEntity;
import cn.chenyunlong.qing.domain.anime.mapper.AnimePlaylistMapper;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.infrastructure.model.dto.PlayListDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final AnimePlaylistMapper animePlaylistMapper;
    private final AnimeEpisodeService episodeService;

    public PlaylistServiceImpl(AnimePlaylistMapper animePlaylistMapper,
                               AnimeEpisodeService episodeService) {
        this.animePlaylistMapper = animePlaylistMapper;
        this.episodeService = episodeService;
    }

    @Override
    public List<PlayListDTO> listPlayListBy(Long animeId) {
        QueryWrapper<PlaylistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anime_id", animeId);
        List<PlaylistEntity> playlistEntities = animePlaylistMapper.selectList(queryWrapper);
        QueryWrapper<AnimeEpisodeEntity> episodeQueryWrapper = new QueryWrapper<>();
        episodeQueryWrapper.eq("anime_id", animeId);
        List<AnimeEpisodeDTO> episodeDTOList = episodeService.listEpisodeByAnimeId(animeId);

        return convertToListVo(playlistEntities, episodeDTOList);
    }

    @Override
    public PlayListDTO getById(Long playlistId) {
        PlaylistEntity playlistEntity = animePlaylistMapper.selectById(playlistId);
        if (playlistEntity == null) {
            return null;
        }
        return new PlayListDTO().convertFrom(playlistEntity);
    }

    public List<PlayListDTO> convertToListVo(List<PlaylistEntity> playlistEntities, List<AnimeEpisodeDTO> episodeEntities) {
        //转换查询到的列表信息
        return playlistEntities.stream().map(playlistEntity ->
        {
            //查询列表中的视频信息
            PlayListDTO playListDTO = new PlayListDTO().convertFrom(playlistEntity);
            playListDTO.setEpisodeList(episodeEntities.stream().filter(animeEpisodeDTO -> animeEpisodeDTO.getPlaylistId().equals(playlistEntity.getId())).collect(Collectors.toList()));
            return playListDTO;
        }).collect(Collectors.toList());
    }


}

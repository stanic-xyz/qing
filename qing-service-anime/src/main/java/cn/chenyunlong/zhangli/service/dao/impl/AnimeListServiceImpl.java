package cn.chenyunlong.zhangli.service.dao.impl;

import cn.chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import cn.chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import cn.chenyunlong.zhangli.service.dao.AnimeListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeListServiceImpl extends ServiceImpl<AnimePlaylistMapper, PlaylistEntity> implements AnimeListService {
    @Override
    public List<PlaylistEntity> getAnimePlayList(Long animeId) {
        return lambdaQuery().eq(PlaylistEntity::getAnimeId, animeId).list();
    }
}

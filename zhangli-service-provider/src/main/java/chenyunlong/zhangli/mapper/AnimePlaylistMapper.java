package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 */
@Mapper
@Component
public interface AnimePlaylistMapper extends BaseMapper<PlaylistEntity> {

    /**
     * 获取动漫播放列表
     *
     * @param animeId 动漫ID
     * @return 动漫的播放列表
     */
    @Select("select * from anime_playlist where anime_id=#{animeId}")
    List<PlaylistEntity> getAnimePlayList(@Param("animeId") Long animeId);
}

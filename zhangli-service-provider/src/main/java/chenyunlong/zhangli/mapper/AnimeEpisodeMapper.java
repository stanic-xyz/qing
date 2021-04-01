package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
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
public interface AnimeEpisodeMapper {
    /**
     * 获取某番剧的播放剧集
     *
     * @param animeId 番剧ID
     * @return 播放剧集信息
     */
    @Select("select * from anime_episode where anime_id=#{animeId}")
    List<AnimeEpisodeEntity> selectByAnimeId(@Param("animeId") Long animeId);
}

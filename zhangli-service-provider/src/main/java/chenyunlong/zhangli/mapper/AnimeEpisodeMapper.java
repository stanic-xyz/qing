package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.entities.anime.AnimeEpisodeEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AnimeEpisodeMapper {
    long countByExample(AnimeEpisodeEntityExample example);

    int deleteByExample(AnimeEpisodeEntityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AnimeEpisodeEntity record);

    int insertSelective(AnimeEpisodeEntity record);

    List<AnimeEpisodeEntity> selectByExample(AnimeEpisodeEntityExample example);

    AnimeEpisodeEntity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AnimeEpisodeEntity record, @Param("example") AnimeEpisodeEntityExample example);

    int updateByExample(@Param("record") AnimeEpisodeEntity record, @Param("example") AnimeEpisodeEntityExample example);

    int updateByPrimaryKeySelective(AnimeEpisodeEntity record);

    int updateByPrimaryKey(AnimeEpisodeEntity record);
}
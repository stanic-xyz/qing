package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AnimeInfoMapper {

    List<AnimeInfo> listAnimes(@Param("page") Pageable pageable);

    Integer count();

    void insertPatch(@Param("animeInfos") List<AnimeInfo> animeInfos);

    /**
     * 获取动漫详情
     *
     * @param movieId 动漫ID
     * @return
     */
    AnimeInfo selectAnimationDetail(String movieId);
}

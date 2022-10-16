package cn.chenyunlong.zhangli.mapper;

import cn.chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Mapper
@Component
public interface AnimePlaylistMapper extends BaseMapper<PlaylistEntity> {

}

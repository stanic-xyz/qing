package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.ActivityEntity;
import chenyunlong.zhangli.model.entities.bilibili.BilibiliAnimeScoreEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 活动信息Mapper
 *
 * @author Stan
 */
@Mapper
@Component
public interface BilibiliAnimeScoreMapper extends BaseMapper<BilibiliAnimeScoreEntity> {

}

package cn.chenyunlong.zhangli.mapper;

import cn.chenyunlong.zhangli.model.entities.bilibili.BilibiliAnimeScoreEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 活动信息Mapper
 *
 * @author Stan
 */
@Mapper
@Component
public interface BilibiliAnimeScoreMapper extends BaseMapper<BilibiliAnimeScoreEntity> {

}

package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.AnimeComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 动画评论
 *
 * @author Stan
 */
@Mapper
@Component
public interface AnimeCommentMapper extends BaseMapper<AnimeComment> {
}

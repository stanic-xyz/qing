package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.AnimeFeedbackEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AnimeFeedbackMapper extends BaseMapper<AnimeFeedbackEntity> {
}

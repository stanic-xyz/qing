package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ActivityMapper extends BaseMapper<Activity> {

    List<Activity> getActivityList();

    Activity getActivityById(Long activityId);
}

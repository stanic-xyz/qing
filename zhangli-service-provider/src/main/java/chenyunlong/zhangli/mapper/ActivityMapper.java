package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.ActivityEntity;
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
public interface ActivityMapper extends BaseMapper<ActivityEntity> {

    /**
     * 获取所有活动信息
     *
     * @return 活动信息列表
     */
    List<ActivityEntity> getActivityList();

    /**
     * 获取具体的活动信息
     *
     * @param activityId 活动ID
     * @return 活动信息
     */
    ActivityEntity getActivityById(Long activityId);
}

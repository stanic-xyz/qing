package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityMapper {

    public List<Activity> getActivityList();
}

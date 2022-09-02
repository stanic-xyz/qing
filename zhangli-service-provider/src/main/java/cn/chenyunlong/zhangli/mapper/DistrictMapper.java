package cn.chenyunlong.zhangli.mapper;

import cn.chenyunlong.zhangli.model.entities.District;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrictMapper extends BaseMapper<District> {
    List<District> getDistrictInfo();
}

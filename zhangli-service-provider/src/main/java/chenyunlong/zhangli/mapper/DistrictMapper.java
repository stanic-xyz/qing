package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.District;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrictMapper {
    List<District> getDistrictInfo();
}

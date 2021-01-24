package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.District;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DistrictMapper {
    List<District> getDistrictInfo();
}

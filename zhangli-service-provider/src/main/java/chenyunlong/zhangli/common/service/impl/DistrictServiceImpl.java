package chenyunlong.zhangli.common.service.impl;

import chenyunlong.zhangli.common.service.DistrictService;
import chenyunlong.zhangli.entities.District;
import chenyunlong.zhangli.mapper.DistrictMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictMapper districtMapper;

    public DistrictServiceImpl(DistrictMapper districtMapper) {
        this.districtMapper = districtMapper;
    }

    @Override
    public List<District> getAllDistrict() {
        return districtMapper.getDistrictInfo();
    }
}

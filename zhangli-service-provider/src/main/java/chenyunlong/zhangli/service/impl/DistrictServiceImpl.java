package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.DistrictMapper;
import chenyunlong.zhangli.model.dto.DistrictDTO;
import chenyunlong.zhangli.model.dto.base.DTOUtil;
import chenyunlong.zhangli.model.entities.District;
import chenyunlong.zhangli.service.DistrictService;
import chenyunlong.zhangli.service.base.AbstractCrudService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Service
public class DistrictServiceImpl extends AbstractCrudService<DistrictMapper, District> implements DistrictService {

    private final DistrictMapper districtMapper;

    public DistrictServiceImpl(DistrictMapper districtMapper) {
        this.districtMapper = districtMapper;
    }

    @Override
    public List<DistrictDTO> getAllDistrict() {
        return lambdaQuery().list().stream().map(domain -> DTOUtil.newDTO(domain, DistrictDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean removeById(@NonNull Serializable id) {
        District district = mustExistById(id);
        return super.removeById(district);
    }
}

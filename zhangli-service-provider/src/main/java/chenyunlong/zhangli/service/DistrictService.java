package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.dto.DistrictDTO;
import chenyunlong.zhangli.model.entities.District;
import chenyunlong.zhangli.service.base.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 * @date 2020/0124
 */
@Service
public interface DistrictService extends CrudService<District> {
    /**
     * 获取所有的地区信息
     *
     * @return 所有的地区信息
     */
    List<DistrictDTO> getAllDistrict();
}

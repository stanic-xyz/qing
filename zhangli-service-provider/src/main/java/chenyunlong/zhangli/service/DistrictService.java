package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.entities.District;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 * @date 2020/0124
 */
@Service
public interface DistrictService {
    /**
     * 获取所有的地区信息
     *
     * @return 所有的地区信息
     */
    List<District> getAllDistrict();
}

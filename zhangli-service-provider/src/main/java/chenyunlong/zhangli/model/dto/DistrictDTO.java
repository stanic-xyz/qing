package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.District;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class DistrictDTO implements OutputConverter<DistrictDTO, District> {

    private Long id;
    private String name;
    private String code;
    private String description;
}

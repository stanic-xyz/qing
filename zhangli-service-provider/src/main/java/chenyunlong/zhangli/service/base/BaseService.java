package chenyunlong.zhangli.service.base;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.utils.BeanUtils;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collections;
import java.util.List;

public interface BaseService<DOMAIN> extends IService<DOMAIN> {


    /**
     * 转换对象
     *
     * @param domain         需要转化的实体类
     * @param targetDtoClass 目标实体类
     * @param <DTO>          DTO实体类的类型
     * @return 转化完成的实体类
     */
    default <DTO extends OutputConverter<DTO, DOMAIN>> DTO toDto(DOMAIN domain, Class<DTO> targetDtoClass) {
        if (domain == null) {
            return null;
        }
        //调用实体类
        return BeanUtils.transformFrom(domain, targetDtoClass);
    }

    default <DTO extends OutputConverter<DTO, DOMAIN>> List<DTO> toListDto(List<DOMAIN> domains, Class<DTO> targetDtoClass) {
        if (domains == null || domains.isEmpty()) {
            return Collections.emptyList();
        }
        return BeanUtils.transformFromInBatch(domains, targetDtoClass);
    }
}

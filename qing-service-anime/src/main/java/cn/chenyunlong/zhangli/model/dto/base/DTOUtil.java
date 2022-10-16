package cn.chenyunlong.zhangli.model.dto.base;

import cn.chenyunlong.zhangli.utils.BeanUtils;

/**
 * @author Stan
 */
public class DTOUtil {


    /**
     * 转换对象
     *
     * @param domain         需要转化的实体类
     * @param targetDtoClass 目标实体类
     * @param <DTO>          DTO实体类的类型
     * @param <DOMAIN>       实体类的类型
     * @return 转化完成的实体类
     */
    public static <DOMAIN, DTO extends OutputConverter<DTO, DOMAIN>> DTO newDTO(DOMAIN domain, Class<DTO> targetDtoClass) {
        if (domain == null) {
            return null;
        }
        //调用实体类
        return BeanUtils.transformFrom(domain, targetDtoClass);
    }
}

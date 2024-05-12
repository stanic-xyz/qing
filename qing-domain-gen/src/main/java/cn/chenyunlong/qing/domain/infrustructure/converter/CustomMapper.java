package cn.chenyunlong.qing.domain.infrustructure.converter;

import cn.chenyunlong.qing.domain.entity.EntityType;
import java.util.Objects;

public class CustomMapper {


    /**
     * int 转 实体类型
     *
     * @param code 类型编码
     * @return 实体类型
     */
    public EntityType int2InOutStoreType(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        return EntityType.of(code).orElse(null);
    }

    /**
     * 类型转化为实体
     *
     * @param inOutStoreType 实体类型
     * @return 入库值
     */
    public Integer inOutStoreType2Int(EntityType inOutStoreType) {
        return inOutStoreType.getValue();
    }
}

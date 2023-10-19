package cn.chenyunlong.qing.domain.entity;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;

/**
 * 实体类型 个人或者团体。
 */
public enum EntityType implements BaseEnum<Integer> {

    USER(1, "个人");

    private final Integer code;
    private final String name;

    EntityType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据Code获取枚举类型。
     *
     * @param code 枚举code
     * @return 枚举类型。
     */
    public static Optional<EntityType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, EntityType.class));
    }

    @Override
    public Integer getValue() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

}

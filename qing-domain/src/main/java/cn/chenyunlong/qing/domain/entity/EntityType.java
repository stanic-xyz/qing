package cn.chenyunlong.qing.domain.entity;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;
import lombok.Getter;

/**
 * 实体类型 个人或者团体。
 */
@Getter
public enum EntityType implements BaseEnum<Integer> {

    USER(1, "个人"),
    COMPANY(2, "企业");

    private final Integer value;

    private final String name;

    EntityType(int code, String name) {
        this.value = code;
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
}

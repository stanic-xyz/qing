package cn.chenyunlong.common.enums;

/**
 * 描述信息
 */
public interface SelfDescribedEnum {
    default String getName() {
        return name();
    }

    String name();

    String getDescription();
}

package chenyunlong.zhangli.model.enums;

import chenyunlong.zhangli.enums.ValueEnum;

/**
 * @author Stan
 */

public enum MessageType implements ValueEnum<Integer> {

    /**
     * 普通消息
     */
    SIMPLE_MESSAGE(0),
    /**
     * 带图片的消息
     */
    MESSAGE_WITH_IMG(1);


    private String typeName;

    private final Integer value;

    MessageType(int i) {
        this.value = i;
    }

    /**
     * Gets enum value.
     *
     * @return enum value
     */
    @Override
    public Integer getValue() {
        return value;
    }
}

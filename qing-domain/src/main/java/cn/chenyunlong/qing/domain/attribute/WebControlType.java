package cn.chenyunlong.qing.domain.attribute;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum WebControlType implements BaseEnum<Integer> {
    //    RADIO(1,"单选"),

    SELECT(2, "单选"),
    MULTI_SELECT(3, "多选"),
    TEXT(4, "文本输入框"),

    UPLOAD(5, "上传控件"),

    CASCADE_SELECT(6, "级联选择器");
    private final Integer code;
    private final String msg;

    WebControlType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getValue() {
        return code;
    }

    @Override
    public String getName() {
        return msg;
    }

    public static Optional<WebControlType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, WebControlType.class));
    }
}

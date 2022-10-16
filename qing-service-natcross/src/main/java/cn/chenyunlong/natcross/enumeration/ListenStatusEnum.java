package cn.chenyunlong.natcross.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 端口运行状态
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 11:39:40
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ListenStatusEnum {

    //
    STOP(1, "未启动"),
    //
    WAIT(5, "已启动但未监听"),
    //
    RUNNING(9, "运行中"),
    //
    ;

    private final Integer code;
    private final String comment;

    ListenStatusEnum(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static ListenStatusEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ListenStatusEnum e : ListenStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

}

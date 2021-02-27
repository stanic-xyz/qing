package chenyunlong.zhangli.common;

/**
 * @author Stan
 */
public class DeferredResultResponse {
    private Integer code;
    private String msg;

    public enum Msg {
        /**
         * 失败
         */
        FAILED("失败"),
        /**
         * 成功
         */
        SUCCESS("成功"),
        /**
         * 超时
         */
        TIMEOUT("超时");

        private String desc;

        Msg(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
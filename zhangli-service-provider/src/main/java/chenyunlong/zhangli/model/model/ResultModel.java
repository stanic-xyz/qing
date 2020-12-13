package chenyunlong.zhangli.model.model;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/**
 * <p>
 * 常规类型的前后端返回model
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:45:41
 */
public class ResultModel {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ResultModel.class);

    public ResultModel() {
    }

    public static ResultModel of(String retCode, String retMsg, Object object) {
        return new ResultModel(retCode, retMsg, object);
    }

    public static ResultModel of(ResultEnum resultEnum, Object data) {
        return new ResultModel(resultEnum.getCode(), resultEnum.getName(), data);
    }

    public static ResultModel of(ResultEnum resultEnum) {
        return new ResultModel(resultEnum.getCode(), resultEnum.getName(), null);
    }

    public static ResultModel ofFail(Object data) {
        return new ResultModel(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getName(), data);
    }

    public static ResultModel ofFail() {
        return new ResultModel(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getName(), null);
    }

    public static ResultModel ofSuccess(Object data) {
        return new ResultModel(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getName(), data);
    }

    public static ResultModel ofSuccess() {
        return new ResultModel(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getName(), null);
    }

    private String retCod;
    private String retMsg;
    private Object data;

    private ResultModel(String retCode, String retMsg, Object object) {
        this.retCod = retCode;
        this.retMsg = retMsg;
        this.data = object;
    }

    /**
     * 反射方式修改值
     *
     * @param fieldStr
     * @param object
     * @return
     * @author wangmin1994@qq.com
     * @since 2019-05-10 14:04:48
     */
    public ResultModel set(String fieldStr, Object object) {
        Field field = null;
        try {
            field = this.getClass().getDeclaredField(fieldStr);
        } catch (NoSuchFieldException | SecurityException e) {
            log.warn("ResultModel get field faild!", e);
            return this;
        }

        field.setAccessible(true);
        try {
            field.set(this, object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.warn("ResultModel set field faild!", e);
            return this;
        }
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getRetCod() {
        return this.retCod;
    }

    public String getRetMsg() {
        return this.retMsg;
    }

    public Object getData() {
        return this.data;
    }

    public ResultModel setRetCod(String retCod) {
        this.retCod = retCod;
        return this;
    }

    public ResultModel setRetMsg(String retMsg) {
        this.retMsg = retMsg;
        return this;
    }

    public ResultModel setData(Object data) {
        this.data = data;
        return this;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResultModel;
    }
}

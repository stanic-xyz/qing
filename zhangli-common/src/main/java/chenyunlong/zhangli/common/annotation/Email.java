package chenyunlong.zhangli.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    /**
     * 接收人
     *
     * @return
     */
    String receiver();

    /**
     * 内容
     *
     * @return
     */
    String content();

    /**
     * 主题
     *
     * @return
     */
    String object();

    /**
     * 模板ID
     *
     * @return
     */
    int templateId() default 1;

    /***
     * 短信类型 ， 1：预约信息，2，收购数据，3：预约语音
     * @return
     */
    int emailType() default 1;
}

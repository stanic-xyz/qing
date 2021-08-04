package chenyunlong.zhangli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonFieldFilter {

    /**
     * 过滤掉不需要的属性
     */
    String[] filters() default {"x_last_changed"};

}

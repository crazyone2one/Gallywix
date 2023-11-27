package cn.master.gallywix.common.result;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释所在的Controller方法无需进行响应体增强
 * @author 11's papa
 * @since 11/27/2023
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
    String value() default "";
}

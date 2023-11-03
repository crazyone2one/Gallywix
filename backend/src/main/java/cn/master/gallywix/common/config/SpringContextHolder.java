package cn.master.gallywix.common.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author 11's papa
 * @since 11/03/2023
 **/
@Slf4j
@Configuration
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    private static void assertContextInjected() {
        if (Objects.isNull(applicationContext)) {
            throw new IllegalStateException("applicationContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (Objects.nonNull(SpringContextHolder.applicationContext)) {
            log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
        }
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * desc: 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param name name of the application context
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        log.debug("从SpringContextHolder中取出Bean:" + name);
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * desc: 通过class获取bean
     *
     * @param name class name
     * @return T
     */
    public static <T> T getBean(Class<T> name) {
        assertContextInjected();
        return applicationContext.getBean(name);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null
     */
    public static void clearHolder() {
        log.info("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
        applicationContext = null;
    }
}

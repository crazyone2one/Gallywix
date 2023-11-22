package cn.master.gallywix.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
@Slf4j
public class CommonBeanFactory implements ApplicationContextAware {
    private static ApplicationContext context;

    public CommonBeanFactory() {
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        context = ctx;
    }

    public static Object getBean(String beanName) {
        try {
            return context != null && !StringUtils.isBlank(beanName) ? context.getBean(beanName) : null;
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> className) {
        try {
            return context != null && className != null ? context.getBean(className) : null;
        } catch (BeansException e) {
            return null;
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> className) {
        return context.getBeansOfType(className);
    }

    public static Object invoke(String beanName, Function<Class, Method> methodFunction, Object... args) {
        Object bean = getBean(beanName);
        try {
            assert bean != null;
            Class<?> clazz = bean.getClass();
            return methodFunction.apply(clazz).invoke(bean, args);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}

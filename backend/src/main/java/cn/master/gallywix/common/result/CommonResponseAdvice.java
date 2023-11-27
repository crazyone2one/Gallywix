package cn.master.gallywix.common.result;

import cn.master.gallywix.utils.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author 11's papa
 * @since 11/27/2023
 **/
@RestControllerAdvice
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 若为以下情况，则不进行响应体封装
        // 1. 该注解存在在方法上
        // 2. 该注解存在在类上
        // 3. 返回体已经是ResponseResult类型

        return !(returnType.getContainingClass().isAnnotationPresent(IgnoreResponseAdvice.class) ||
                Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class) ||
                returnType.getParameterType().isAssignableFrom(ResponseResult.class));
    }

    /**
     * 响应体增强
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型需要特别处理，否则会发生类型转换异常
        if (body instanceof String) {
            // 设置响应头
            response.getHeaders().add("Content-Type", "application/json");
            return JsonUtils.toJsonPrettyString(ResponseResult.success(body));
        }
        return ResponseResult.success(body);
    }
}

package cn.master.gallywix.auth.config;

import cn.master.gallywix.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证失败
 *
 * @author 11's papa
 * @since 11/01/2023
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class UnAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final RedisUtils redisUtils;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        // Here you can place any message you want
        log.error("[认证异常处理]用户未认证 => Error logging in : {1} ", authException);
        redisUtils.delete("accessToken");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}

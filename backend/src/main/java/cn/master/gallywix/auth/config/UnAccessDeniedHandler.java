package cn.master.gallywix.auth.config;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * @author 11's papa
 * @since 11/01/2023
 **/
@Component
public class UnAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        // This is invoked when user tries to access a secured REST resource without the necessary authorization
        // We should just send a 403 Forbidden response because there is no 'error' page to redirect to
        // Here you can place any message you want

        ServletUtils.renderString(response,
                ResponseResult.fail(HttpServletResponse.SC_FORBIDDEN, "user access denied")
        );
    }
}

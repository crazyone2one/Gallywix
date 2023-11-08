package cn.master.gallywix.auth.config;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.utils.ServletUtils;
import cn.master.gallywix.utils.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author 11's papa
 * @since 11/08/2023
 **/
@Component
public class AuthLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 清空上下文信息
        SessionUtils.clearContext();
        ServletUtils.renderString(response, ResponseResult.success(), 200);
    }
}

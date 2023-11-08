package cn.master.gallywix.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

/**
 * @author 11's papa
 * @since 11/08/2023
 **/
@Slf4j
public class ServletUtils {
    public static void renderString(HttpServletResponse response, Object object) {
        renderString(response, JsonUtils.toJsonString(object));
    }

    public static void renderString(HttpServletResponse response, String json) {
        try {
            response.setStatus(200);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(json);
        } catch (Exception e) {
            log.error("响应失败", e);
        }
    }
}

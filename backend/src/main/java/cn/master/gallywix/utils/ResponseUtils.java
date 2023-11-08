package cn.master.gallywix.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 11's papa
 * @since 11/08/2023
 **/
@Slf4j
public class ResponseUtils {
    public static void renderString(HttpServletResponse response, Object object) {
        renderString(response, JsonUtils.toJsonString(object));
    }
    public static void renderString(HttpServletResponse response,String json){
        try {
            response.setStatus(200);
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(json);
        } catch (Exception e) {
            log.error("响应失败",e);
        }
    }
}

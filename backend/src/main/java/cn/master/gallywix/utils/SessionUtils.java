package cn.master.gallywix.utils;

import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.query.QueryChain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;

/**
 * @author 11's papa
 * @since 11/02/2023
 **/
@Slf4j
public class SessionUtils {
    private static final ThreadLocal<String> PROJECT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> WORKSPACE_ID = new ThreadLocal<>();

    public static UserDetails sessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static SystemUser sessionUserInfo() {
        Authentication authentication = getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return QueryChain.of(SystemUser.class).where(SYSTEM_USER.USERNAME.eq(principal.getUsername())).one();
    }

    public static String getUserId() {
        return Optional.ofNullable(sessionUserInfo()).map(SystemUser::getId).orElse(null);
    }

    public static void clearContext() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(context);
    }

    public static String getCurrentWorkspaceId() {
        if (StringUtils.isNotEmpty(WORKSPACE_ID.get())) {
            return WORKSPACE_ID.get();
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            log.debug("WORKSPACE: {}", request.getHeader("WORKSPACE"));
            if (Objects.nonNull(request.getHeader("WORKSPACE"))) {
                return request.getHeader("WORKSPACE");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.ofNullable(sessionUserInfo()).orElse(new SystemUser()).getLastWorkspaceId();
    }

    public static String getCurrentProjectId() {
        if (StringUtils.isNotEmpty(PROJECT_ID.get())) {
            return PROJECT_ID.get();
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            log.debug("PROJECT: {}", request.getHeader("PROJECT"));
            if (Objects.nonNull(request.getHeader("PROJECT"))) {
                return request.getHeader("PROJECT");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.ofNullable(sessionUserInfo()).orElse(new SystemUser()).getLastProjectId();
    }
}

package cn.master.gallywix.utils;

import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.query.QueryChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;

/**
 * @author 11's papa
 * @since 11/02/2023
 **/
public class SessionUtils {
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

    public static void clearContext() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(context);
    }
}

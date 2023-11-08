package cn.master.gallywix.auth.config;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.utils.RedisUtils;
import cn.master.gallywix.utils.ServletUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @author 11's papa
 * @since 10/31/2023
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        // 登录接口
        if ("/auth/authenticate".equals(uri) || "/auth/refresh-token".equals(uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        val accessToken = jwtProvider.resolveToken(request);
        if (Objects.isNull(accessToken) || StringUtils.isBlank(redisUtils.getString(accessToken))) {
            ServletUtils.renderString(response, ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(), "请先登录"), HttpStatus.UNAUTHORIZED.value());
            return;
        }
        Claims claims;
        // 验证token
        try {
            claims = jwtProvider.resolveClaims(request);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            ServletUtils.renderString(response, ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(), "JWT token is expired"), HttpStatus.UNAUTHORIZED.value());
            return;
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT token.");
            ServletUtils.renderString(response, ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(), "Unsupported JWT token"), HttpStatus.UNAUTHORIZED.value());
            return;
        } catch (Exception ex) {
            request.setAttribute("invalid", ex.getMessage());
            ServletUtils.renderString(response, ResponseResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return;
        }
        assert claims != null;
        if (jwtProvider.validateClaims(claims)) {
            // 将认证成功的用户保存到上下文
            String username = claims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

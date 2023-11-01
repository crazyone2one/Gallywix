package cn.master.gallywix.auth.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            val accessToken = jwtProvider.resolveToken(request);
            if (Objects.isNull(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            val claims = jwtProvider.resolveClaims(request);
            if (claims != null & jwtProvider.validateClaims(claims)) {
                String username = claims.getSubject();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, "", new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.", e);
            //HANDLE IT HERE::::: wrap ExpiredJwtException in AuthenticationException and rethrow Exception
            throw new CredentialsExpiredException("Expired jwt credentials ", e);
        }
        filterChain.doFilter(request, response);
    }
}

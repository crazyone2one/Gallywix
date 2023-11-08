package cn.master.gallywix.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 11's papa
 * @since 10/31/2023
 **/
@Slf4j
@Component
public class JwtProvider {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String generateToken(CustomUserDetail user) {
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(CustomUserDetail user) {
        return buildToken(user, refreshExpiration);
    }

    public Claims resolveClaims(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token != null) {
            return parseJwtClaims(token);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request) {
        String tokenHeader = "Authorization";
        String bearerToken = request.getHeader(tokenHeader);
        String tokenPrefix = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    private Claims parseJwtClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private String buildToken(CustomUserDetail user, long expiration) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Date tokenCreateTime = new Date(System.currentTimeMillis());
        Date tokenValidity = new Date(tokenCreateTime.getTime() + expiration);
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
}

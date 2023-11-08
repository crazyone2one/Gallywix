package cn.master.gallywix.controller;

import cn.master.gallywix.auth.config.CustomUserDetail;
import cn.master.gallywix.auth.config.JwtProvider;
import cn.master.gallywix.auth.module.AuthenticationRequest;
import cn.master.gallywix.auth.module.AuthenticationResponse;
import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/01/2023
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @ResponseBody
    @PostMapping("/authenticate")
    public ResponseResult<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            val authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SessionUtils.setAuthentication(authenticate);
            //  处理用户权限
            Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();
            List<String> collect = authorities.stream().map(GrantedAuthority::getAuthority).toList();
            CustomUserDetail principal = (CustomUserDetail) authenticate.getPrincipal();
            val accessToken = jwtProvider.generateToken(principal);
            val refreshToken = jwtProvider.generateRefreshToken(principal);
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(principal)
                    .roles(collect)
                    .build();
            return ResponseResult.success(response);
        } catch (AuthenticationException e) {
            return ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(), "invalid username or password");
        }
    }

    @GetMapping("demo1")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult<String> demo() {
        SystemUser user = SessionUtils.sessionUserInfo();
        return ResponseResult.success("current user: {},{}" + user.getUsername() + user.getId());
    }
}

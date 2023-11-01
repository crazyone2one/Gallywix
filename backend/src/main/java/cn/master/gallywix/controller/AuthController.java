package cn.master.gallywix.controller;

import cn.master.gallywix.auth.config.JwtProvider;
import cn.master.gallywix.auth.module.AuthenticationRequest;
import cn.master.gallywix.auth.module.AuthenticationResponse;
import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.mapper.SystemUserMapper;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;

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
    private final SystemUserMapper systemUserMapper;

    @ResponseBody
    @PostMapping("/authenticate")
    public ResponseResult<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            val authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            val name = authenticate.getName();
            val accessToken = jwtProvider.generateToken(SystemUser.builder().username(name).build());
            val refreshToken = jwtProvider.generateRefreshToken(SystemUser.builder().username(name).build());
            QueryWrapper wrapper = QueryWrapper.create().where(SYSTEM_USER.USERNAME.eq(name));
            val systemUser = systemUserMapper.selectOneByQuery(wrapper);
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .accessToken(accessToken).refreshToken(refreshToken).user(systemUser).build();
            return ResponseResult.success(response);
        } catch (Exception exception) {
            return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), "invalid username or password");
        }
    }

    @GetMapping("demo1")
    public ResponseResult<String> demo() {
        return ResponseResult.success("demo1");
    }
}

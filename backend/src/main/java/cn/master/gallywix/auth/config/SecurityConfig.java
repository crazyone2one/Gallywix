package cn.master.gallywix.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author 11's papa
 * @since 10/31/2023
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final UnAccessDeniedHandler unAccessDeniedHandler;
    private final UnAuthenticationEntryPoint unAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return
                // 关闭csrf
                security.csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(request -> request
                                // 配置不需要认证的请求
                                .requestMatchers("/auth/authenticate", "/auth/refresh-token").anonymous()
//                                .requestMatchers("/auth/demo1").hasAuthority("ADMIN")
                                .requestMatchers("/demo1").hasAnyRole("admin")
                                .anyRequest().authenticated())
                        // 不通过session获取security context
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                        // 异常处理
                        .exceptionHandling(exception -> exception
                                // 认证失败
                                .authenticationEntryPoint(unAuthenticationEntryPoint)
                                // 权限不正确
                                .accessDeniedHandler(unAccessDeniedHandler))
                        .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

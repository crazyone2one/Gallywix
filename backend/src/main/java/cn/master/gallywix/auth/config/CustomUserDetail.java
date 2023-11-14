package cn.master.gallywix.auth.config;

import cn.master.gallywix.entity.SystemUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/08/2023
 **/
public class CustomUserDetail implements UserDetails {
    @Getter
    private final SystemUser systemUser;
    @Getter
    private final String id;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetail(SystemUser systemUser) {
         this(systemUser, Collections.emptyList());
    }

    public CustomUserDetail(SystemUser systemUser, List<GrantedAuthority> permissions) {
        this.systemUser = systemUser;
        this.id = systemUser.getId();
        this.authorities = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return systemUser.getPassword();
    }

    @Override
    public String getUsername() {
        return systemUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return systemUser.getStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        return systemUser.getStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return systemUser.getStatus();
    }

    @Override
    public boolean isEnabled() {
        return systemUser.getStatus();
    }
}

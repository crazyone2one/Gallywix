package cn.master.gallywix.auth.config;

import cn.master.gallywix.entity.SystemUser;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/08/2023
 **/
public class CustomUserDetail implements UserDetails {
    private final SystemUser systemUser;
    @Getter
    private final String id;
    private List<GrantedAuthority> authorities;
    public CustomUserDetail(SystemUser systemUser, List<String> permissions) {
        this.systemUser = systemUser;
        this.id = systemUser.getId();
        if (CollectionUtils.isNotEmpty(permissions)) {
            this.authorities = new ArrayList<>();
            permissions.forEach(role -> this.authorities.add(new SimpleGrantedAuthority(role)));
        }
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

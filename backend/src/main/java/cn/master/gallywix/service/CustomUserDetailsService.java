package cn.master.gallywix.service;

import cn.master.gallywix.auth.config.CustomUserDetail;
import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;

/**
 * @author 11's papa
 * @since 10/31/2023
 **/
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = QueryChain.of(SystemUser.class).where(SYSTEM_USER.USERNAME.eq(username)).one();
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("invalid username or password");
        }
        // TODO: 2023/10/31 设置用户角色
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        roles.add("ADMIN");
        return new CustomUserDetail(user, roles);
    }
}

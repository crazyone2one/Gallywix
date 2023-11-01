package cn.master.gallywix.service;

import cn.master.gallywix.mapper.SystemUserMapper;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;

/**
 * @author 11's papa
 * @since 10/31/2023
 **/
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final SystemUserMapper systemUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper wrapper = QueryWrapper.create().from(SYSTEM_USER)
                .where(SYSTEM_USER.USERNAME.eq(username));
        val systemUser = systemUserMapper.selectOneByQuery(wrapper);
        // TODO: 2023/10/31 设置用户角色
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return org.springframework.security.core.userdetails.User.builder()
                .username(systemUser.getUsername())
                .password(systemUser.getPassword())
                .roles(roles.toArray(new String[0]))
                .disabled(systemUser.getStatus())
                .build();
    }
}

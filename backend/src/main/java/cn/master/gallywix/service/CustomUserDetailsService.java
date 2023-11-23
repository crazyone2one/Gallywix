package cn.master.gallywix.service;

import cn.master.gallywix.auth.config.CustomUserDetail;
import cn.master.gallywix.common.config.SpringContextHolder;
import cn.master.gallywix.dto.GroupResourceDTO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.UserGroupPermission;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (!user.getStatus()) {
            throw new RuntimeException("user_has_been_disabled");
        }
        UserDTO userDTO = SpringContextHolder.getBean(ISystemUserService.class).getUserDTO(user.getId());
        List<String> roles = new ArrayList<>();
        List<GroupResourceDTO> groupPermissions = userDTO.getGroupPermissions();
        if (CollectionUtils.isNotEmpty(groupPermissions)) {
            List<List<UserGroupPermission>> list = groupPermissions.stream().map(GroupResourceDTO::getUserGroupPermissions).toList();
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(p -> {
                            List<String> list1 = p.stream().map(UserGroupPermission::getPermissionId).toList();
                            roles.addAll(list1);
                        }
                );
            }
        }
        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new CustomUserDetail(user, authorities);
    }
}

package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemRole;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.UserRole;
import cn.master.gallywix.mapper.SystemUserMapper;
import cn.master.gallywix.service.ISystemUserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static cn.master.gallywix.entity.table.SystemRoleTableDef.SYSTEM_ROLE;
import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.gallywix.entity.table.UserRoleTableDef.USER_ROLE;

/**
 * 用户信息表 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO saveUser(SystemUser user) {
        checkUserParam(user);
        createUser(user);
        return getUserDTO(user.getId());
    }

    @Override
    public Page<SystemUser> findDataByPage(UserPageReqVO page) {
        QueryWrapper wrapper = QueryWrapper.create().where(SYSTEM_USER.USERNAME.like(page.getName()));
        return mapper.paginate(page.getPageNumber(),page.getPageSize(),wrapper);
    }

    private UserDTO getUserDTO(String id) {
        SystemUser user = QueryChain.of(SystemUser.class).where(SYSTEM_USER.ID.eq(id)).one();
        if (Objects.isNull(user)) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        List<UserRole> userRoleList = QueryChain.of(UserRole.class).where(USER_ROLE.USER_ID.eq(id)).list();
        if (CollectionUtils.isEmpty(userRoleList)) {
            return userDTO;
        }
        List<String> roleIds = userRoleList.stream().map(UserRole::getRoleId).toList();
        List<SystemRole> systemRoles = QueryChain.of(SystemRole.class).where(SYSTEM_ROLE.ID.in(roleIds)).list();
        userDTO.setRoles(systemRoles);
        return userDTO;
    }

    public void createUser(SystemUser user) {
//        user.setStatus(true);
        mapper.insert(user);
    }

    private void checkUserParam(SystemUser user) {
        if (StringUtils.isBlank(user.getUsername())) {
            CustomException.throwException("user_name_empty");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            CustomException.throwException("user_email_empty");
        } else {
            long count = QueryChain.of(SystemUser.class).where(SYSTEM_USER.EMAIL.eq(user.getEmail())).count();
            if (count > 0) {
                CustomException.throwException("user_email_is_exist");
            }
        }
    }
}

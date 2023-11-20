package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.RoleConstants;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.member.QueryMemberRequest;
import cn.master.gallywix.controller.vo.user.AddOrgMemberRequestVO;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemRole;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.SystemWorkspace;
import cn.master.gallywix.entity.UserRole;
import cn.master.gallywix.mapper.SystemUserMapper;
import cn.master.gallywix.mapper.SystemWorkspaceMapper;
import cn.master.gallywix.mapper.UserRoleMapper;
import cn.master.gallywix.service.ISystemUserService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.master.gallywix.entity.table.SystemRoleTableDef.SYSTEM_ROLE;
import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.gallywix.entity.table.UserGroupTableDef.USER_GROUP;
import static cn.master.gallywix.entity.table.UserRoleTableDef.USER_ROLE;

/**
 * 用户信息表 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {
    final UserRoleMapper userRoleMapper;
    final SystemWorkspaceMapper workspaceMapper;

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
        return mapper.paginate(page.getPageNumber(), page.getPageSize(), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrganizationMember(AddOrgMemberRequestVO request) {
        if (CollectionUtils.isNotEmpty(request.getUserIds())) {
            for (String userId : request.getUserIds()) {
                UserRole userRole = UserRole.builder().roleId(RoleConstants.ORG_ADMIN)
                        .sourceId(request.getOrganizationId())
                        .userId(userId).build();
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOrganizationMember(String organizationId, String userId) {
        userRoleMapper.deleteByQuery(
                new QueryWrapper().where(USER_ROLE.ROLE_ID.eq(RoleConstants.ORG_OTHER).
                        and(USER_ROLE.SOURCE_ID.eq(organizationId))
                        .and(USER_ROLE.USER_ID.eq(userId))));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void switchUserRole(String sign, String sourceId) {
        SystemUser user = SessionUtils.sessionUserInfo();
        if (StringUtils.equals("organization", sign)) {
            user.setLastProjectId(sourceId);
            user.setLastWorkspaceId("");
        }
        if (StringUtils.equals("workspace", sign)) {
            SystemWorkspace workspace = workspaceMapper.selectOneById(sourceId);
            user.setLastProjectId(workspace.getOrganizationId());
            user.setLastWorkspaceId(sourceId);
        }
        mapper.update(user);
    }

    @Override
    public Map<String, SystemUser> queryNameByIds(List<String> userIds) {
        if (userIds.isEmpty()) {
            return new HashMap<>(0);
        }
        return mapper.queryNameByIds(userIds);
    }

    @Override
    public void refreshSessionUser(String sign, String sourceId) {
        SystemUser user = mapper.selectOneById(SessionUtils.getUserId());
        if (StringUtils.equals("organization", sign)) {
            user.setLastProjectId(StringUtils.EMPTY);
        }
        if (StringUtils.equals("workspace", sign) && StringUtils.equals(sourceId, user.getLastWorkspaceId())) {
            user.setLastWorkspaceId(StringUtils.EMPTY);
        }
        mapper.update(user);
    }

    @Override
    public List<SystemUser> getMemberList(QueryMemberRequest request) {
        QueryWrapper wrapper = new QueryWrapper();
        QueryWrapper query = new QueryWrapper()
                .select("distinct *").from(
                        wrapper.select(SYSTEM_USER.ALL_COLUMNS)
                                .from(USER_GROUP).join(SYSTEM_USER).on(USER_GROUP.USER_ID.eq(SYSTEM_USER.ID))
                                .where(USER_GROUP.SOURCE_ID.eq(request.getWorkspaceId()))
                                .and(SYSTEM_USER.USERNAME.like(request.getName(), StringUtils.isNoneBlank(request.getName())))
                                .orderBy(USER_GROUP.UPDATE_TIME.desc())
                ).as("temp");
        return mapper.selectListByQuery(query);
    }

    @Override
    public Page<SystemUser> findWsMemberByPage(UserPageReqVO page) {
        QueryWrapper wrapper = new QueryWrapper();
        QueryWrapper query = new QueryWrapper()
                .select("distinct *").from(
                        wrapper.select(SYSTEM_USER.ALL_COLUMNS)
                                .from(USER_GROUP).join(SYSTEM_USER).on(USER_GROUP.USER_ID.eq(SYSTEM_USER.ID))
                                .where(USER_GROUP.SOURCE_ID.eq(page.getWorkspaceId()))
                                .and(SYSTEM_USER.USERNAME.like(page.getName(), StringUtils.isNoneBlank(page.getName())))
                                .orderBy(USER_GROUP.UPDATE_TIME.desc())
                ).as("temp");
        return mapper.paginate(page, query);
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

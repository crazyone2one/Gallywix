package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.RoleConstants;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.user.AddOrgMemberRequestVO;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.controller.vo.user.UserPasswordVO;
import cn.master.gallywix.dto.GroupResourceDTO;
import cn.master.gallywix.dto.UserGroupPermissionDTO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.*;
import cn.master.gallywix.mapper.*;
import cn.master.gallywix.service.ISystemUserService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cn.master.gallywix.entity.table.SystemGroupTableDef.SYSTEM_GROUP;
import static cn.master.gallywix.entity.table.SystemProjectTableDef.SYSTEM_PROJECT;
import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.gallywix.entity.table.UserGroupPermissionTableDef.USER_GROUP_PERMISSION;
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
    private final UserGroupMapper userGroupMapper;
    private final SystemGroupMapper systemGroupMapper;
    private final UserGroupPermissionMapper userGroupPermissionMapper;
    final SystemProjectMapper projectMapper;
    @Lazy
    final PasswordEncoder passwordEncoder;

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
    public void switchUserResource(String sign, String sourceId) {
        SystemUser user = SessionUtils.sessionUserInfo();
        if (StringUtils.equals("organization", sign)) {
            user.setLastProjectId(sourceId);
            user.setLastWorkspaceId("");
        }
        if (StringUtils.equals("workspace", sign)) {
            user.setLastProjectId(StringUtils.EMPTY);
            user.setLastWorkspaceId(sourceId);
            List<SystemProject> projects = getProjectListByWsAndUserId(user.getId(), sourceId);
            if (CollectionUtils.isNotEmpty(projects)) {
                user.setLastProjectId(projects.get(0).getId());
            }
        }
        mapper.update(user);
    }

    private List<SystemProject> getProjectListByWsAndUserId(String userId, String workspaceId) {
        List<SystemProject> projects = projectMapper.selectListByQuery(QueryChain.of(SystemProject.class)
                .where(SYSTEM_PROJECT.WORKSPACE_ID.eq(workspaceId)));
        List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class)
                .where(USER_GROUP.USER_ID.eq(userId)));
        List<SystemProject> projectList = new ArrayList<>();
        userGroups.forEach(userGroup -> projects.forEach(project -> {
            if (StringUtils.equals(userGroup.getSourceId(), project.getId())) {
                if (!projectList.contains(project)) {
                    projectList.add(project);
                }
            }
        }));
        return projectList;
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
    public List<SystemUser> getMemberList(UserPageReqVO request) {
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
                                .and(SYSTEM_USER.USERNAME.like(page.getName()))
                                .orderBy(USER_GROUP.UPDATE_TIME.desc())
                ).as("temp");
        return mapper.paginate(page, query);
    }

    @Override
    public Page<SystemUser> getProjectMemberList(UserPageReqVO request) {
        QueryWrapper wrapper = new QueryWrapper();
        QueryWrapper query = new QueryWrapper()
                .select("distinct *").from(
                        wrapper.select(SYSTEM_USER.ALL_COLUMNS)
                                .from(USER_GROUP).join(SYSTEM_USER).on(USER_GROUP.USER_ID.eq(SYSTEM_USER.ID))
                                .where(USER_GROUP.SOURCE_ID.eq(request.getProjectId()))
                                .orderBy(SYSTEM_USER.UPDATE_TIME.desc())
                ).as("temp");
        return mapper.paginate(request, query);
    }

    @Override
    public UserGroupPermissionDTO getUserGroup(String userId) {
        UserGroupPermissionDTO userGroupPermissionDTO = new UserGroupPermissionDTO();
        List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class).where(USER_GROUP.USER_ID.eq(userId)));
        if (CollectionUtils.isEmpty(userGroups)) {
            return userGroupPermissionDTO;
        }
        userGroupPermissionDTO.setUserGroups(userGroups);
        List<String> groupId = userGroups.stream().map(UserGroup::getGroupId).toList();
        List<SystemGroup> groups = systemGroupMapper.selectListByQuery(QueryChain.of(SystemGroup.class).where(SYSTEM_GROUP.CODE.in(groupId)));
        userGroupPermissionDTO.setGroups(groups);
        return userGroupPermissionDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUser updateUser(SystemUser systemUser) {
        mapper.update(systemUser);
        return systemUser;
    }

    @Override
    public boolean updateUserPassword(UserPasswordVO request) {
        return UpdateChain.of(SystemUser.class).set(SYSTEM_USER.PASSWORD, passwordEncoder.encode(request.getNewPassword()))
                .where(SYSTEM_USER.ID.eq(request.getId())).update();
    }

    @Override
    public UserDTO getUserDTO(String id) {
        SystemUser user = QueryChain.of(SystemUser.class).where(SYSTEM_USER.ID.eq(id)).one();
        if (Objects.isNull(user)) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        UserGroupPermissionDTO dto = getUserGroupPermission(user.getId());
        userDTO.setUserGroups(dto.getUserGroups());
        userDTO.setGroups(dto.getGroups());
        userDTO.setGroupPermissions(dto.getList());
        return userDTO;
    }

    private UserGroupPermissionDTO getUserGroupPermission(String id) {
        UserGroupPermissionDTO permissionDTO = new UserGroupPermissionDTO();
        List<GroupResourceDTO> list = new ArrayList<>();
        List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class).where(USER_GROUP.USER_ID.eq(id)));
        if (CollectionUtils.isEmpty(userGroups)) {
            return permissionDTO;
        }
        permissionDTO.setUserGroups(userGroups);
        List<String> groupList = userGroups.stream().map(UserGroup::getGroupId).toList();
        List<SystemGroup> groups = systemGroupMapper.selectListByQuery(QueryChain.of(SystemGroup.class).where(SYSTEM_GROUP.CODE.in(groupList)));
        permissionDTO.setGroups(groups);
        groups.forEach(gp -> {
            GroupResourceDTO dto = new GroupResourceDTO();
            dto.setGroup(gp);
            List<UserGroupPermission> userGroupPermissions = userGroupPermissionMapper
                    .selectListByQuery(QueryChain.of(UserGroupPermission.class).where(USER_GROUP_PERMISSION.GROUP_ID.eq(gp.getCode())));
            dto.setUserGroupPermissions(userGroupPermissions);
            list.add(dto);
        });
        permissionDTO.setList(list);
        return permissionDTO;
    }

    public void createUser(SystemUser user) {
//        user.setStatus(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

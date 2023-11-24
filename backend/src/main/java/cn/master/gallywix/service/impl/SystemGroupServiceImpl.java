package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.UserGroupConstants;
import cn.master.gallywix.common.constants.UserGroupType;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.controller.vo.group.EditGroupUserRequest.EditGroupUserRequest;
import cn.master.gallywix.controller.vo.group.GroupPageReqVO;
import cn.master.gallywix.controller.vo.group.GroupRequest.GroupRequest;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.controller.vo.workspace.AddMemberRequest.AddMemberRequest;
import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.dto.GroupPermission;
import cn.master.gallywix.dto.WorkspaceResource;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.UserGroup;
import cn.master.gallywix.entity.UserGroupPermission;
import cn.master.gallywix.mapper.SystemGroupMapper;
import cn.master.gallywix.mapper.SystemUserMapper;
import cn.master.gallywix.mapper.UserGroupMapper;
import cn.master.gallywix.mapper.UserGroupPermissionMapper;
import cn.master.gallywix.service.ISystemGroupService;
import cn.master.gallywix.service.ISystemUserService;
import cn.master.gallywix.service.ISystemWorkspaceService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

import static cn.master.gallywix.entity.table.SystemGroupTableDef.SYSTEM_GROUP;
import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.gallywix.entity.table.UserGroupPermissionTableDef.USER_GROUP_PERMISSION;
import static cn.master.gallywix.entity.table.UserGroupTableDef.USER_GROUP;

/**
 * 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SystemGroupServiceImpl extends ServiceImpl<SystemGroupMapper, SystemGroup> implements ISystemGroupService {
    private final UserGroupMapper userGroupMapper;
    private final UserGroupPermissionMapper userGroupPermissionMapper;
    private final ISystemWorkspaceService workspaceService;
    private final SystemUserMapper systemUserMapper;
    private final SystemGroupMapper systemGroupMapper;
    final ISystemUserService systemUserService;
    private static final String GLOBAL = "global";
    private static final Map<String, List<String>> MAP = new HashMap<>(4) {{
        put(UserGroupType.SYSTEM, Arrays.asList(UserGroupType.SYSTEM, UserGroupType.WORKSPACE, UserGroupType.PROJECT));
        put(UserGroupType.WORKSPACE, Arrays.asList(UserGroupType.WORKSPACE, UserGroupType.PROJECT));
        put(UserGroupType.PROJECT, Collections.singletonList(UserGroupType.PROJECT));
    }};

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemGroup addGroup(EditGroupRequest request) {
        checkGroupExist(request);
        request.setCreator(SessionUtils.getUserId());
        request.setSystem(false);
        if (BooleanUtils.isTrue(request.getGlobal())) {
            request.setScopeId(GLOBAL);
        }
        mapper.insert(request);
        return mapper.selectOneById(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemGroup editGroup(EditGroupRequest request) {
        if (StringUtils.equals(request.getName(), UserGroupConstants.SUPER_GROUP)) {
            CustomException.throwException("超级管理员无法编辑");
        }
        if (StringUtils.equals(request.getName(), UserGroupConstants.ADMIN)) {
            CustomException.throwException("系统管理员无法编辑");
        }
        checkGroupExist(request);
        mapper.update(request);
        return mapper.selectOneById(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteGroup(Serializable id) {
        SystemGroup group = mapper.selectOneById(id);
        if (Objects.isNull(group)) {
            CustomException.throwException("group does not exist!");
        }
        if (BooleanUtils.isTrue(group.getSystem())) {
            CustomException.throwException("系统用户组不支持删除");
        }
        mapper.delete(group);
        QueryWrapper userGroupQuery = QueryWrapper.create().from(UserGroup.class)
                .where(USER_GROUP.GROUP_ID.eq(group.getId()));
        userGroupMapper.deleteByQuery(userGroupQuery);
        QueryWrapper userGroupPermissionQuery = QueryWrapper.create().from(UserGroupPermission.class)
                .where(USER_GROUP_PERMISSION.GROUP_ID.eq(group.getId()));
        userGroupPermissionMapper.deleteByQuery(userGroupPermissionQuery);
        return null;
    }

    @Override
    public Page<GroupDTO> getGroupPageList(GroupPageReqVO page) {
        SystemUser user = SessionUtils.sessionUserInfo();
        List<UserGroup> userGroups = QueryChain.of(UserGroup.class)
                .select(USER_GROUP.USER_ID, USER_GROUP.GROUP_ID, USER_GROUP.SOURCE_ID)
                .select(SYSTEM_GROUP.NAME, SYSTEM_GROUP.TYPE.as("type"))
                .from(USER_GROUP).leftJoin(SYSTEM_GROUP).on(USER_GROUP.GROUP_ID.eq(SYSTEM_GROUP.CODE))
                .where(USER_GROUP.USER_ID.eq(user.getId()))
                .where(USER_GROUP.SOURCE_ID.eq(page.getProjectId(), StringUtils.isNoneBlank(page.getProjectId())))
                .list();
        List<String> groupTypeList = userGroups.stream().map(UserGroup::getType).distinct().toList();
        return getGroups(groupTypeList, page);
    }

    @Override
    public List<SystemGroup> getGroupByType(EditGroupRequest request) {
        String type = request.getType();
        if (StringUtils.isBlank(type)) {
            return new ArrayList<>();
        }
        return mapper.selectListByQuery(queryChain()
                .where(SYSTEM_GROUP.TYPE.eq(type, !StringUtils.equals(type, UserGroupType.SYSTEM)))
                .where(SYSTEM_GROUP.SCOPE_ID.eq(GLOBAL, BooleanUtils.isTrue(request.isOnlyQueryGlobal()))));
    }

    @Override
    public List<Map<String, Object>> getAllUserGroup(String userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class)
                .where(USER_GROUP.USER_ID.eq(userId)));
        List<String> groupsIds = userGroups.stream().map(UserGroup::getGroupId).distinct().toList();
        groupsIds.forEach(id -> {
            SystemGroup group = mapper.selectOneByQuery(queryChain().where(SYSTEM_GROUP.CODE.eq(id)));
            String type = group.getType();
            Map<String, Object> map = new HashMap<>(2);
            map.put("type", id + "+" + type);
            WorkspaceResource workspaceResource = workspaceService.listResource(id, group.getType());
            List<String> collect = userGroups.stream().filter(ugp -> ugp.getGroupId().equals(id)).map(UserGroup::getSourceId).toList();
            map.put("ids", collect);
            if (StringUtils.equals(type, UserGroupType.WORKSPACE)) {
                map.put("workspaces", workspaceResource.getWorkspaces());
            }
            if (StringUtils.equals(type, UserGroupType.PROJECT)) {
                map.put("projects", workspaceResource.getProjects());
            }
            list.add(map);
        });
        return list;
    }

    @Override
    public List<SystemGroup> getGroupsByType(GroupRequest request) {
        String resourceId = request.getResourceId();
        String type = request.getType();
        List<String> scopeList = Arrays.asList(GLOBAL, resourceId);
        if (StringUtils.equals(type, UserGroupType.PROJECT) && StringUtils.isNotBlank(request.getProjectId())) {
            scopeList = Arrays.asList(GLOBAL, resourceId, request.getProjectId());
        }
        return mapper.selectListByQuery(queryChain().where(SYSTEM_GROUP.SCOPE_ID.in(scopeList)).and(SYSTEM_GROUP.TYPE.eq(type)));
    }

    @Override
    public String addMember(AddMemberRequest request) {
        if (CollectionUtils.isEmpty(request.getUserIds()) || CollectionUtils.isEmpty(request.getGroupIds())) {
            log.warn("user ids or group ids is empty.");
            return "user ids or group ids is empty";
        }
        List<String> allUserIds = systemUserMapper.selectListByQuery(QueryChain.of(SystemUser.class)).stream().map(SystemUser::getId).toList();
        List<String> wsGroupIds = systemGroupMapper.selectListByQuery(QueryChain.of(SystemGroup.class).where(SYSTEM_GROUP.TYPE.eq(UserGroupType.WORKSPACE)))
                .stream().map(SystemGroup::getId).toList();
        request.getUserIds().forEach(userId -> {
            if (!allUserIds.contains(userId)) {
                log.warn("user id {} not exist.", userId);
                return;
            }
            List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class)
                    .where(USER_GROUP.USER_ID.eq(userId).and(USER_GROUP.SOURCE_ID.in(request.getWorkspaceId()))));
            if (!userGroups.isEmpty()) {
                CustomException.throwException("user_already_exists");
            }
            for (String groupId : request.getGroupIds()) {
                if (!wsGroupIds.contains(groupId)) {
                    log.warn("group id {} is not exist or not belong to workspace level.", groupId);
                    continue;
                }
                UserGroup userGroup = UserGroup.builder().groupId(groupId)
                        .sourceId(request.getWorkspaceId())
                        .userId(userId).build();
                userGroupMapper.insert(userGroup);
            }
        });
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String editGroupPermission(EditGroupRequest request) {
        // 超级用户组禁止修改权限
        if (StringUtils.equals(request.getUserGroupId(), UserGroupConstants.SUPER_GROUP)) {
            return "超级用户组禁止修改权限";
        }
        List<GroupPermission> permissions = request.getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return "权限为空";
        }
        userGroupPermissionMapper.deleteByQuery(QueryChain.of(UserGroupPermission.class)
                .where(USER_GROUP_PERMISSION.GROUP_ID.eq(request.getUserGroupId())));
        String groupId = request.getUserGroupId();
        permissions.forEach(permission -> {
            if (BooleanUtils.isTrue(permission.getChecked())) {
                UserGroupPermission build = UserGroupPermission.builder().permissionId(permission.getId())
                        .moduleId(permission.getResourceId())
                        .groupId(groupId).build();
                userGroupPermissionMapper.insert(build);
            }
        });
        return "success";
    }

    @Override
    public Page<SystemUser> getGroupUser(UserPageReqVO request) {
        return systemUserMapper.paginate(request, QueryChain.of(SystemUser.class)
                .select("DISTINCT `tb_system_user`.`id`,\n" +
                        "       `tb_system_user`.`username`,\n" +
                        "       `tb_system_user`.`email`,\n" +
                        "       `tb_system_user`.`phone`,\n" +
                        "       `tb_user_group`.update_time")
                .join(USER_GROUP).on(USER_GROUP.USER_ID.eq(SYSTEM_USER.ID))
                .where(USER_GROUP.GROUP_ID.eq(request.getUserGroupId()))
                .where(SYSTEM_USER.USERNAME.like(request.getName()))
                .where(USER_GROUP.SOURCE_ID.eq(request.getProjectId()))
                .orderBy(USER_GROUP.UPDATE_TIME, false));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addGroupUser(EditGroupUserRequest request) {
        if (StringUtils.isBlank(request.getGroupId()) || CollectionUtils.isEmpty(request.getUserIds())) {
            return "add group user warning, please check param";
        }
        SystemGroup group = mapper.selectOneById(request.getGroupId());
        if (Objects.isNull(group)) {
            return "add group user warning, group is null. group id: " + request.getGroupId();
        }
        if (StringUtils.equals(group.getType(), UserGroupType.SYSTEM)) {
            SystemUser user = Objects.requireNonNull(SessionUtils.sessionUserInfo());
            UserDTO userDTO = systemUserService.getUserDTO(user.getId());
            long count = userDTO.getGroups().stream().filter(g -> StringUtils.equals(g.getType(), UserGroupType.SYSTEM)).count();
            if (count > 0) {
                addSystemGroupUser(group, request.getUserIds());
            } else {
                return "no permission to add system group!";
            }
        } else {
            if (CollectionUtils.isNotEmpty(request.getSourceIds())) {
                addNotSystemGroupUser(group, request.getUserIds(), request.getSourceIds());
            }
        }
        return "success";
    }

    private void addNotSystemGroupUser(SystemGroup group, List<String> userIds, List<String> sourceIds) {
        for (String userId : userIds) {
            SystemUser user = systemUserMapper.selectOneById(userId);
            if (user == null) {
                continue;
            }
            List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class).where(USER_GROUP.USER_ID.eq(userId))
                    .and(USER_GROUP.GROUP_ID.eq(group.getId())));
            List<String> existSourceIds = userGroups.stream().map(UserGroup::getSourceId).toList();
            List<String> toAddSourceIds = new ArrayList<>(sourceIds);
            toAddSourceIds.removeAll(existSourceIds);
            for (String sourceId : toAddSourceIds) {
                UserGroup userGroup = UserGroup.builder().userId(userId).groupId(group.getId()).sourceId(sourceId).build();
                userGroupMapper.insert(userGroup);
            }
        }
    }

    private void addSystemGroupUser(SystemGroup group, List<String> userIds) {
        for (String userId : userIds) {
            SystemUser user = systemUserMapper.selectOneById(userId);
            if (user == null) {
                continue;
            }
            List<UserGroup> userGroups = userGroupMapper.selectListByQuery(QueryChain.of(UserGroup.class).where(USER_GROUP.USER_ID.eq(userId))
                    .and(USER_GROUP.GROUP_ID.eq(group.getId())));
            if (userGroups.isEmpty()) {
                UserGroup userGroup = UserGroup.builder().userId(userId).groupId(group.getId()).sourceId("system").build();
                userGroupMapper.insert(userGroup);
            }
        }
    }

    private Page<GroupDTO> getGroups(List<String> groupTypeList, GroupPageReqVO page) {
        if (groupTypeList.contains(UserGroupType.SYSTEM)) {
            return getUserGroup(UserGroupType.SYSTEM, page);
        }
        if (groupTypeList.contains(UserGroupType.WORKSPACE)) {
            return getUserGroup(UserGroupType.WORKSPACE, page);
        }
        if (groupTypeList.contains(UserGroupType.PROJECT)) {
            return getUserGroup(UserGroupType.PROJECT, page);
        }
        return new Page<>();
    }

    private Page<GroupDTO> getUserGroup(String groupType, GroupPageReqVO page) {
        List<String> types;
        String workspaceId = SessionUtils.getCurrentWorkspaceId();
        String projectId = SessionUtils.getCurrentProjectId();
        List<String> scopes = Arrays.asList(GLOBAL, workspaceId, projectId);
        if (StringUtils.equals(groupType, UserGroupType.SYSTEM)) {
            scopes = new ArrayList<>();
        }
        types = MAP.get(groupType);
        page.setTypes(types);
        page.setScopes(scopes);
        QueryWrapper wrapper = QueryWrapper.create().where(SYSTEM_GROUP.TYPE.in(types))
                .and(SYSTEM_GROUP.SCOPE_ID.in(scopes));
        Map<String, Object> otherParams = new HashMap<>();
        otherParams.put("types", types);
        otherParams.put("name", page.getName());
        otherParams.put("orders", page.getOrders());
        return mapper.xmlPaginate("getGroupList", Page.of(page.getPageNumber(), page.getPageSize()), wrapper, otherParams);
    }

    private void checkGroupExist(EditGroupRequest request) {
        boolean exists = QueryChain.of(SystemGroup.class).where(SYSTEM_GROUP.NAME.eq(request.getName()))
                .where(SYSTEM_GROUP.ID.ne(request.getId()).when(StringUtils.isNoneBlank(request.getId())))
                .exists();
        if (exists) {
            CustomException.throwException("用户组名称已存在");
        }
    }
}

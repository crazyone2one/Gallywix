package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.UserGroupConstants;
import cn.master.gallywix.common.constants.UserGroupType;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.controller.vo.group.GroupPageReqVO;
import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.UserGroup;
import cn.master.gallywix.entity.UserGroupPermission;
import cn.master.gallywix.mapper.SystemGroupMapper;
import cn.master.gallywix.mapper.UserGroupMapper;
import cn.master.gallywix.mapper.UserGroupPermissionMapper;
import cn.master.gallywix.service.ISystemGroupService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

import static cn.master.gallywix.entity.table.SystemGroupTableDef.SYSTEM_GROUP;
import static cn.master.gallywix.entity.table.UserGroupPermissionTableDef.USER_GROUP_PERMISSION;
import static cn.master.gallywix.entity.table.UserGroupTableDef.USER_GROUP;

/**
 * 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SystemGroupServiceImpl extends ServiceImpl<SystemGroupMapper, SystemGroup> implements ISystemGroupService {
    private final UserGroupMapper userGroupMapper;
    private final UserGroupPermissionMapper userGroupPermissionMapper;
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
        Map<String,Object> otherParams  = new HashMap<>();
        otherParams .put("types", types);
        otherParams .put("name", page.getName());
        otherParams .put("orders", page.getOrders());
        return mapper.xmlPaginate("getGroupList", Page.of(page.getPageNumber(), page.getPageSize()), wrapper,otherParams );
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

package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.UserGroupConstants;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.workspace.WorkspacePageReqVO;
import cn.master.gallywix.entity.SystemProject;
import cn.master.gallywix.entity.SystemWorkspace;
import cn.master.gallywix.entity.UserGroup;
import cn.master.gallywix.mapper.SystemGroupMapper;
import cn.master.gallywix.mapper.SystemProjectMapper;
import cn.master.gallywix.mapper.SystemWorkspaceMapper;
import cn.master.gallywix.mapper.UserGroupMapper;
import cn.master.gallywix.service.ISystemUserService;
import cn.master.gallywix.service.ISystemWorkspaceService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.master.gallywix.entity.table.SystemGroupTableDef.SYSTEM_GROUP;
import static cn.master.gallywix.entity.table.SystemProjectTableDef.SYSTEM_PROJECT;
import static cn.master.gallywix.entity.table.SystemWorkspaceTableDef.SYSTEM_WORKSPACE;
import static cn.master.gallywix.entity.table.UserGroupTableDef.USER_GROUP;

/**
 * 工作空间信息 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SystemWorkspaceServiceImpl extends ServiceImpl<SystemWorkspaceMapper, SystemWorkspace> implements ISystemWorkspaceService {
    private final UserGroupMapper userGroupMapper;
    private final ISystemUserService systemUserService;
    private final SystemProjectMapper systemProjectMapper;
    private final SystemGroupMapper systemGroupMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemWorkspace add(SystemWorkspace workspace) {
        checkWorkspace(workspace);
        mapper.insert(workspace);
        UserGroup userGroup = UserGroup.builder().userId(SessionUtils.getUserId())
                .sourceId(workspace.getId())
                .groupId(UserGroupConstants.WS_ADMIN).build();
        userGroupMapper.insert(userGroup);
        return workspace;
    }

    private void checkWorkspace(SystemWorkspace workspace) {
        if (StringUtils.isBlank(workspace.getName())) {
            CustomException.throwException("Workspace name cannot be null.");
        }
        if (workspace.getName().length() > 100) {
            CustomException.throwException("workspace_name_exceeds_length_limit");
        }
        boolean exists = QueryChain.of(SystemWorkspace.class)
                .where(SYSTEM_WORKSPACE.NAME.eq(workspace.getName(), StringUtils.isNoneBlank(workspace.getName())))
                .exists();
        if (exists) {
            CustomException.throwException("workspace_name_already_exists");
        }
    }

    @Override
    public Page<SystemWorkspace> findDataByPage(WorkspacePageReqVO page) {
        QueryWrapper wrapper = QueryWrapper.create().where(SYSTEM_WORKSPACE.NAME.like(page.getName()));
        return mapper.paginate(page.getPageNumber(), page.getPageSize(), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteWorkspace(String id) {
        systemUserService.refreshSessionUser("workspace", id);
        // delete project
        List<SystemProject> projects = QueryChain.of(SystemProject.class).where(SYSTEM_PROJECT.WORKSPACE_ID.eq(id)).list();
        List<String> projectIdList = new java.util.ArrayList<>(projects.stream().map(SystemProject::getId).toList());
        if (CollectionUtils.isNotEmpty(projectIdList)) {
            systemProjectMapper.deleteBatchByIds(projectIdList);
        }
        // delete group
        projectIdList.add(id);
        QueryWrapper groupWrapper = QueryChain.create().where(SYSTEM_GROUP.SCOPE_ID.in(projectIdList));
        systemGroupMapper.deleteByQuery(groupWrapper);
        // delete user group
        QueryWrapper userGroupWrapper = QueryChain.create().where(USER_GROUP.SOURCE_ID.in(projectIdList));
        userGroupMapper.deleteByQuery(userGroupWrapper);
        // delete workspace
        mapper.deleteById(id);
        return null;
    }
}

package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.UserGroupConstants;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.project.ProjectPageReqVO;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.entity.ProjectVersion;
import cn.master.gallywix.entity.SystemProject;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.UserGroup;
import cn.master.gallywix.mapper.ProjectVersionMapper;
import cn.master.gallywix.mapper.SystemProjectMapper;
import cn.master.gallywix.mapper.UserGroupMapper;
import cn.master.gallywix.service.ISystemProjectService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static cn.master.gallywix.entity.table.SystemProjectTableDef.SYSTEM_PROJECT;
import static cn.master.gallywix.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.gallywix.entity.table.UserGroupTableDef.USER_GROUP;

/**
 * 项目信息 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SystemProjectServiceImpl extends ServiceImpl<SystemProjectMapper, SystemProject> implements ISystemProjectService {
    private final UserGroupMapper userGroupMapper;
    private final ProjectVersionMapper projectVersionMapper;
    private final ISystemUserService systemUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemProject saveProject(SystemProject systemProject) {
        if (StringUtils.isBlank(systemProject.getName())) {
            CustomException.throwException("Project name cannot be null");
        }
        boolean exists = QueryChain.of(SystemProject.class).
                where(SYSTEM_PROJECT.NAME.eq(systemProject.getName()))
                .and(SYSTEM_PROJECT.WORKSPACE_ID.eq(systemProject.getWorkspaceId())).exists();
        if (exists) {
            CustomException.throwException("Project name already exists");
        }
        systemProject.setSystemId(genSystemId());
        mapper.insert(systemProject);
        // 创建项目为当前用户添加用户组
        UserGroup userGroup = UserGroup.builder()
                .userId(SessionUtils.getUserId())
                .groupId(UserGroupConstants.PROJECT_ADMIN)
                .sourceId(systemProject.getId()).build();
        userGroupMapper.insert(userGroup);
        UpdateChain.of(SystemUser.class)
                .set(SYSTEM_USER.LAST_PROJECT_ID, systemProject.getId())
                .where(SYSTEM_USER.ID.eq(SessionUtils.getUserId()))
                .and(SYSTEM_USER.LAST_PROJECT_ID.isNull().or(SYSTEM_USER.LAST_PROJECT_ID.eq("")))
                .update();
        // 创建默认版本
        addProjectVersion(systemProject);
        return systemProject;
    }

    private void addProjectVersion(SystemProject project) {
        ProjectVersion projectVersion = ProjectVersion.builder()
                .name("v1.0.0")
                .projectId(project.getId())
                .startTime(LocalDateTime.now())
                .publishTime(LocalDateTime.now())
                .status("open")
                .latest(true)
                .createUser(SessionUtils.getUserId())
                .build();
        projectVersionMapper.insert(projectVersion);
    }

    private String genSystemId() {
        String maxSystemIdInDb = getMaxSystemId();
        String systemId = "10001";
        if (StringUtils.isNotEmpty(maxSystemIdInDb)) {
            systemId = String.valueOf(Long.parseLong(maxSystemIdInDb) + 1);
        }
        return systemId;
    }

    @Override
    public String getMaxSystemId() {
        return mapper.getMaxSystemId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProject(String id) {
        // User Group
        userGroupMapper.deleteByQuery(QueryWrapper.create().where(USER_GROUP.SOURCE_ID.eq(id)));
        return mapper.deleteById(id);
    }

    @Override
    public Page<SystemProject> getProjectPageList(ProjectPageReqVO page) {
        QueryChain<SystemProject> queryChain = QueryChain.of(SystemProject.class)
                .where(SYSTEM_PROJECT.NAME.like(page.getName()))
                .where(SYSTEM_PROJECT.WORKSPACE_ID.eq(page.getWorkspaceId()));
        Page<SystemProject> paginate = mapper.paginate(page, queryChain);
        List<SystemProject> records = paginate.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(project -> {
                UserPageReqVO reqVO = new UserPageReqVO();
                reqVO.setProjectId(project.getId());
                project.setMemberSize(systemUserService.getProjectMemberList(reqVO).getRecords().size());
            });
        }
        return paginate;
    }

}

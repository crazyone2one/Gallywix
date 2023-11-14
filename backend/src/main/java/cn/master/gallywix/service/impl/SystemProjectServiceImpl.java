package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.SystemProject;
import cn.master.gallywix.mapper.SystemProjectMapper;
import cn.master.gallywix.service.ISystemProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.master.gallywix.entity.table.SystemProjectTableDef.SYSTEM_PROJECT;

/**
 * 项目信息 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class SystemProjectServiceImpl extends ServiceImpl<SystemProjectMapper, SystemProject> implements ISystemProjectService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveProject(SystemProject systemProject) {
        if (StringUtils.isBlank(systemProject.getName())) {
            CustomException.throwException("Project name cannot be null");
        }
        boolean exists = QueryChain.of(SystemProject.class).
                where(SYSTEM_PROJECT.NAME.eq(systemProject.getName()))
                .and(SYSTEM_PROJECT.WORKSPACE_ID.eq(SessionUtils.getCurrentWorkspaceId())).exists();
        if (exists) {
            CustomException.throwException("Project name already exists");
        }
        systemProject.setWorkspaceId(SessionUtils.getCurrentWorkspaceId());
        return mapper.insert(systemProject);
    }

}

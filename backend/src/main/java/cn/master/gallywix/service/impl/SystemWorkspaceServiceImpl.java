package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.workspace.WorkspacePageReqVO;
import cn.master.gallywix.entity.SystemWorkspace;
import cn.master.gallywix.mapper.SystemWorkspaceMapper;
import cn.master.gallywix.service.ISystemWorkspaceService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.master.gallywix.entity.table.SystemWorkspaceTableDef.SYSTEM_WORKSPACE;

/**
 * 工作空间信息 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class SystemWorkspaceServiceImpl extends ServiceImpl<SystemWorkspaceMapper, SystemWorkspace> implements ISystemWorkspaceService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemWorkspace add(SystemWorkspace workspace) {
        if (StringUtils.isBlank(workspace.getName())) {
            CustomException.throwException("Workspace name cannot be null.");
        }
        // TODO 组织ID 暂无
        if (StringUtils.isBlank(workspace.getOrganizationId())) {
            workspace.setOrganizationId("root");
        }
        mapper.insert(workspace);
        return workspace;
    }

    @Override
    public Page<SystemWorkspace> findDataByPage(WorkspacePageReqVO page) {
        QueryWrapper wrapper = QueryWrapper.create().where(SYSTEM_WORKSPACE.NAME.like(page.getName()));
        return mapper.paginate(page.getPageNumber(), page.getPageSize(), wrapper);
    }
}

package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.workspace.WorkspacePageReqVO;
import cn.master.gallywix.entity.SystemWorkspace;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 * 工作空间信息 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemWorkspaceService extends IService<SystemWorkspace> {

    SystemWorkspace add(SystemWorkspace systemWorkspace);

    Page<SystemWorkspace> findDataByPage(WorkspacePageReqVO page);
}

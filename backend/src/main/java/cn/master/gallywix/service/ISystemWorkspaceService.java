package cn.master.gallywix.service;

import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.SystemWorkspace;

/**
 * 工作空间信息 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemWorkspaceService extends IService<SystemWorkspace> {

    SystemWorkspace add(SystemWorkspace systemWorkspace);
}

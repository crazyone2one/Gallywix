package cn.master.gallywix.service;

import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.SystemProject;

/**
 * 项目信息 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemProjectService extends IService<SystemProject> {

    int saveProject(SystemProject systemProject);
}

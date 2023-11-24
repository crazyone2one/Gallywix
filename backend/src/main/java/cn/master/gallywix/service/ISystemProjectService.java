package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.project.ProjectPageReqVO;
import cn.master.gallywix.entity.SystemProject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 项目信息 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemProjectService extends IService<SystemProject> {

    SystemProject saveProject(SystemProject systemProject);

    String getMaxSystemId();

    int deleteProject(String id);

    Page<SystemProject> getProjectPageList(ProjectPageReqVO page);

    List<SystemProject> getUserProject(ProjectPageReqVO request);
}

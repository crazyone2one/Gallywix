package cn.master.gallywix.controller.vo.project;

import cn.master.gallywix.controller.vo.OrderRequest;
import cn.master.gallywix.entity.SystemProject;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author 11's papa
 * @since 11/13/2023
 **/
@Setter
@Getter
public class ProjectPageReqVO extends Page<SystemProject> {
    private String workspaceId;
    private String projectId;
    private String userId;
    private String name;
    private List<OrderRequest> orders;
    private Map<String, List<String>> filters;
    private Map<String, Object> combine;
}

package cn.master.gallywix.controller.vo.workspace;

import cn.master.gallywix.entity.SystemWorkspace;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/06/2023
 **/
@Setter
@Getter
public class WorkspacePageReqVO extends Page<SystemWorkspace> {
    private String name;
}

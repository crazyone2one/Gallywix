package cn.master.gallywix.controller.vo.project;

import cn.master.gallywix.entity.SystemProject;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/13/2023
 **/
@Setter
@Getter
public class ProjectPageReqVO extends Page<SystemProject> {
    private String name;
}

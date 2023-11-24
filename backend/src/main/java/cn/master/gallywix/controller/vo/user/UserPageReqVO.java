package cn.master.gallywix.controller.vo.user;

import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/06/2023
 **/
@Setter
@Getter
public class UserPageReqVO extends Page<SystemUser> {
    private String name;
    private String workspaceId;
    private String projectId;
    private String userGroupId;
}

package cn.master.gallywix.controller.vo.group;

import cn.master.gallywix.controller.vo.OrderRequest;
import cn.master.gallywix.dto.GroupPermission;
import cn.master.gallywix.entity.SystemGroup;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
@Setter
@Getter
public class GroupPageReqVO extends Page<SystemGroup> {
    private List<String> types = new ArrayList<>();
    private List<String> scopes = new ArrayList<>();
    /**
     * 是否是全局用户组
     */
    private Boolean global;

    private String projectId;
    private String userGroupId;
    private boolean onlyQueryCurrentProject = false;
    private boolean onlyQueryGlobal = false;
    private List<GroupPermission> permissions;
    private List<OrderRequest> orders;
    private String name;
}

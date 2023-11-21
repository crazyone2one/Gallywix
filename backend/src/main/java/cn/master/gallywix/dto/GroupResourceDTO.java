package cn.master.gallywix.dto;

import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.UserGroupPermission;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/21/2023
 **/
@Data
public class GroupResourceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private GroupResource resource;
    private List<GroupPermission> permissions;
    private String type;

    private SystemGroup group;
    private List<UserGroupPermission> userGroupPermissions;
}

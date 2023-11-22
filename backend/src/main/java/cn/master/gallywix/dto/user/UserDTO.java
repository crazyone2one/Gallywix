package cn.master.gallywix.dto.user;

import cn.master.gallywix.dto.GroupResourceDTO;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.entity.UserGroup;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/02/2023
 **/
@Setter
@Getter
public class UserDTO extends SystemUser {
    private List<UserGroup> userGroups = new ArrayList<>();
    private List<SystemGroup> groups = new ArrayList<>();
    private List<GroupResourceDTO> groupPermissions = new ArrayList<>();

    @Serial
    private static final long serialVersionUID = 1L;
}

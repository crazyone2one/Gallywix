package cn.master.gallywix.dto;

import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.UserGroup;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/22/2023
 **/
@Data
public class UserGroupPermissionDTO {
    List<GroupResourceDTO> list = new ArrayList<>();
    List<SystemGroup> groups = new ArrayList<>();
    List<UserGroup> userGroups = new ArrayList<>();
}

package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.dto.GroupPermissionDTO;
import cn.master.gallywix.entity.SystemGroup;
import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.UserGroupPermission;

/**
 *  服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface IUserGroupPermissionService extends IService<UserGroupPermission> {

    String editGroupPermission(EditGroupRequest editGroupRequest);

    GroupPermissionDTO getGroupResource(SystemGroup group);
}

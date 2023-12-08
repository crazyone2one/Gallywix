package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.controller.vo.group.EditGroupUserRequest.EditGroupUserRequest;
import cn.master.gallywix.controller.vo.group.GroupPageReqVO;
import cn.master.gallywix.controller.vo.group.GroupRequest.GroupRequest;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.controller.vo.workspace.AddMemberRequest.AddMemberRequest;
import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *  服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemGroupService extends IService<SystemGroup> {

    SystemGroup addGroup(EditGroupRequest request);

    SystemGroup editGroup(EditGroupRequest request);

    String deleteGroup(Serializable id);

    Page<GroupDTO> getGroupPageList(GroupPageReqVO page);

    List<SystemGroup> getGroupByType(EditGroupRequest request);

    List<Map<String, Object>> getAllUserGroup(String userId);

    List<SystemGroup> getGroupsByType(GroupRequest request);

    String addMember(AddMemberRequest request);

    String editGroupPermission(EditGroupRequest editGroupRequest);

    Page<SystemUser> getGroupUser(UserPageReqVO request);

    String addGroupUser(EditGroupUserRequest request);

    List<SystemGroup> getWorkspaceMemberGroups(String workspaceId, String userId);
}

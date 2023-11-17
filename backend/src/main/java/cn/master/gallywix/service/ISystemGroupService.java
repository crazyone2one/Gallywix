package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.controller.vo.group.GroupPageReqVO;
import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.entity.SystemGroup;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.io.Serializable;

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
}

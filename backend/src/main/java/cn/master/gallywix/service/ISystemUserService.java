package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.member.QueryMemberRequest;
import cn.master.gallywix.controller.vo.user.AddOrgMemberRequestVO;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemUserService extends IService<SystemUser> {

    UserDTO saveUser(SystemUser systemUser);

    Page<SystemUser> findDataByPage(UserPageReqVO page);

    void addOrganizationMember(AddOrgMemberRequestVO request);

    void delOrganizationMember(String organizationId, String userId);

    void switchUserRole(String sign, String sourceId);

    Map<String, SystemUser> queryNameByIds(List<String> userIds);

    void refreshSessionUser(String sign, String sourceId);

    List<SystemUser> getMemberList(QueryMemberRequest request);
}

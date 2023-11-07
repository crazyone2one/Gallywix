package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemUser;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 * 用户信息表 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemUserService extends IService<SystemUser> {

    UserDTO saveUser(SystemUser systemUser);

    Page<SystemUser> findDataByPage(UserPageReqVO page);
}

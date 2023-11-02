package cn.master.gallywix.service;

import cn.master.gallywix.dto.user.UserDTO;
import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.SystemUser;

/**
 * 用户信息表 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemUserService extends IService<SystemUser> {

    UserDTO saveUser(SystemUser systemUser);
}

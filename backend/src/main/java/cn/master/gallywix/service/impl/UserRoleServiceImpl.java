package cn.master.gallywix.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.UserRole;
import cn.master.gallywix.mapper.UserRoleMapper;
import cn.master.gallywix.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}

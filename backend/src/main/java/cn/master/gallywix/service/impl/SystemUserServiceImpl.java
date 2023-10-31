package cn.master.gallywix.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.mapper.SystemUserMapper;
import cn.master.gallywix.service.ISystemUserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息表 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

}

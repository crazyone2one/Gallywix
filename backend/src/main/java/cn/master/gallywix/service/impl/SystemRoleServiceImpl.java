package cn.master.gallywix.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.SystemRole;
import cn.master.gallywix.mapper.SystemRoleMapper;
import cn.master.gallywix.service.ISystemRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色信息 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements ISystemRoleService {

}

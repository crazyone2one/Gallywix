package cn.master.gallywix.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.UserGroup;
import cn.master.gallywix.mapper.UserGroupMapper;
import cn.master.gallywix.service.IUserGroupService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements IUserGroupService {

}

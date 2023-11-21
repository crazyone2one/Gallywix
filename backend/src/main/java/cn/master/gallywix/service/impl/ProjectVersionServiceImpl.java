package cn.master.gallywix.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.ProjectVersion;
import cn.master.gallywix.mapper.ProjectVersionMapper;
import cn.master.gallywix.service.IProjectVersionService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class ProjectVersionServiceImpl extends ServiceImpl<ProjectVersionMapper, ProjectVersion> implements IProjectVersionService {

}

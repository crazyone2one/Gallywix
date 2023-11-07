package cn.master.gallywix.service.impl;

import cn.master.gallywix.controller.vo.organization.OrganizationReqVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.gallywix.entity.SystemOrganization;
import cn.master.gallywix.mapper.SystemOrganizationMapper;
import cn.master.gallywix.service.ISystemOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.master.gallywix.entity.table.SystemOrganizationTableDef.SYSTEM_ORGANIZATION;

/**
 * 组织信息 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class SystemOrganizationServiceImpl extends ServiceImpl<SystemOrganizationMapper, SystemOrganization> implements ISystemOrganizationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemOrganization addOrganization(SystemOrganization systemOrganization) {
        mapper.insert(systemOrganization);
        return systemOrganization;
    }

    @Override
    public Page<SystemOrganization> findDataByPage(OrganizationReqVO page) {
        QueryWrapper wrapper = QueryWrapper.create().where(SYSTEM_ORGANIZATION.NAME.like(page.getName()));
        return mapper.paginate(page.getPageNumber(),page.getPageSize(),wrapper);
    }
}

package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.organization.OrganizationReqVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.SystemOrganization;

/**
 * 组织信息 服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ISystemOrganizationService extends IService<SystemOrganization> {

    SystemOrganization addOrganization(SystemOrganization systemOrganization);

    Page<SystemOrganization> findDataByPage(OrganizationReqVO page);
}

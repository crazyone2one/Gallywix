package cn.master.gallywix.controller.vo.organization;

import cn.master.gallywix.entity.SystemOrganization;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/07/2023
 **/
@Setter
@Getter
public class OrganizationReqVO extends Page<SystemOrganization> {
    private String name;
}

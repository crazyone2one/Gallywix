package cn.master.gallywix.service;

import cn.master.gallywix.dto.CustomFieldDao;
import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.IssueTemplate;

/**
 *  服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface IIssueTemplateService extends IService<IssueTemplate> {

    void handleSystemFieldCreate(CustomFieldDao customFieldDao);
}
